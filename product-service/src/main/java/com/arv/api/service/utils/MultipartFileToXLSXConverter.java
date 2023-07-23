package com.arv.api.service.utils;

import com.arv.api.helper.PictureDataMultipartFile;
import com.arv.api.model.dto.request.ProductItemDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MultipartFileToXLSXConverter {
    public List<ProductItemDTO> convert(MultipartFile multipartFile, String storeId, String majorCategoryId) throws Exception {
        try (InputStream inputStream = multipartFile.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            String outputFilePath = "uploaded-file.xlsx";
            try (Workbook outputWorkbook = new XSSFWorkbook()) {
                List<?> images = workbook.getSheetAt(0).getWorkbook().getAllPictures();
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet inputSheet = workbook.getSheetAt(i);
                    Sheet outputSheet = outputWorkbook.createSheet(inputSheet.getSheetName());
                    copySheet(inputSheet, outputSheet);
                }

                try (OutputStream outputStream = Files.newOutputStream(Paths.get(outputFilePath))) {
                    outputWorkbook.write(outputStream);
                    return getProductItemDTOList(Optional.of(outputWorkbook), images, storeId, majorCategoryId);
                }
            }
        }
    }

    private void copySheet(Sheet inputSheet, Sheet outputSheet) {
        for (Row inputRow : inputSheet) {
            Row outputRow = outputSheet.createRow(inputRow.getRowNum());
            for (Cell inputCell : inputRow) {
                Cell outputCell = outputRow.createCell(inputCell.getColumnIndex(), inputCell.getCellType());
                switch (inputCell.getCellType()) {
                    case STRING:
                        outputCell.setCellValue(inputCell.getStringCellValue());
                        break;
                    case NUMERIC:
                        outputCell.setCellValue(inputCell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        outputCell.setCellValue(inputCell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        outputCell.setCellFormula(inputCell.getCellFormula());
                        break;
                }
            }
        }
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private List<ProductItemDTO> getProductItemDTOList(Optional<Workbook> uploadedData, List<?> images, String storeId, String majorCategoryId) throws Exception {
        if (!uploadedData.isPresent()) {
            throw new Exception("Unable to upload");
        }

        Sheet sheet = uploadedData.get().getSheetAt(0);
        List<ProductItemDTO> productItemDTOList = new ArrayList<>();
        int i = 0;
        for (Row row : sheet) {
            if (row.getCell(0) == null || row.getRowNum() == 0) continue;
            String name = row.getCell(0).getStringCellValue();
            String description = getStringValueByIndex(row, 1, "-");
            String category = row.getCell(2).getStringCellValue();
            String categoryTag = getStringValueByIndex(row, 3, category);
            String subCategory = getStringValueByIndex(row, 4, category);
            Double mrpPrice = (Double) getValueByIndex(row, 5);
            Double sellingPrice = (Double) getValueByIndex(row, 6);
            Double discount = (Double) getValueByIndex(row, 7);
            String productVariation = getStringValueByIndex(row, 8, "-");
            Integer stock = getIntegerValue(row);
            String id = row.getCell(0).getStringCellValue().toLowerCase().replaceAll(" ", "-");
            String tags = row.getCell(11).getStringCellValue().toLowerCase();

            ProductItemDTO itemDTO = new ProductItemDTO();
            itemDTO.setStoreId(storeId);
            itemDTO.setId(storeId + id);
            itemDTO.setProductName(name);
            itemDTO.setDescription(description);
            itemDTO.setCategoryNameOrId(category);
            itemDTO.setSubCategoryTags(categoryTag);
            itemDTO.setSubCategoryNameOrId(subCategory);
            itemDTO.setMrpPrice(mrpPrice);
            itemDTO.setSellingPrice(sellingPrice);
            itemDTO.setDiscount(discount);
            itemDTO.setProductVariation(productVariation);
            itemDTO.setStock(stock);
            itemDTO.setMajorCategoryId(majorCategoryId);
            if (images.size() > i)
                itemDTO.setImageFile(convertToMultipartFile((PictureData) images.get(i++)));
            itemDTO.setTags(tags);
            productItemDTOList.add(itemDTO);
        }
        return productItemDTOList;
    }

    private Object getValueByIndex(Row row, int index) {
        try {
            return row.getCell(index).getNumericCellValue();
        } catch (Exception e) {
            return 0.0;
        }
    }

    private String getStringValueByIndex(Row row, int index, String defaultValue) {
        try {
            return row.getCell(index).getStringCellValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private Integer getIntegerValue(Row row) {
        try {
            return (int) row.getCell(9).getNumericCellValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public MultipartFile convertToMultipartFile(PictureData picture) {
        String filename = "image." + picture.suggestFileExtension();
        return new PictureDataMultipartFile(picture, filename);
    }
}
