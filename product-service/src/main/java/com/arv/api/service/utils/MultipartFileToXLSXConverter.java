package com.arv.api.service.utils;

import com.arv.api.helper.PictureDataMultipartFile;
import com.arv.api.model.dto.request.product.ProductItemDTO;
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
    public List<ProductItemDTO> convert(MultipartFile multipartFile) throws Exception {
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
                    return getProductItemDTOList(Optional.of(outputWorkbook), images);
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
    private List<ProductItemDTO> getProductItemDTOList(Optional<Workbook> uploadedData, List<?> images) throws Exception {
        if (!uploadedData.isPresent()) {
            throw new Exception("Unable to upload");
        }

        Sheet sheet = uploadedData.get().getSheetAt(0);
        List<ProductItemDTO> productItemDTOList = new ArrayList<>();
        int i = 0;
        for (Row row : sheet) {
            if (row.getCell(0) == null || row.getRowNum() == 0) continue;
            String name = row.getCell(0).getStringCellValue();
            String description = (String) getValueByIndex(row, 1, "-");
            String category = row.getCell(2).getStringCellValue();
            String categoryTag = getValueByIndex(row, 3, null) == null ? category : row.getCell(4).getStringCellValue();
            String subCategory = getValueByIndex(row, 4, null) == null ? category : row.getCell(5).getStringCellValue();
            Double mrpPrice = (Double) getValueByIndex(row, 5, (double) 0);
            Double sellingPrice = (Double) getValueByIndex(row, 6, (double) 0);
            Double discount = (Double) getValueByIndex(row, 7, (double) 0);
            String productVariation = (String) getValueByIndex(row, 8, "-");
            Integer stock = getIntegerValue(row, 9);
            String id = row.getCell(0).getStringCellValue().toLowerCase().replaceAll(" ", "-");
            String tags = row.getCell(11).getStringCellValue().toLowerCase();

            ProductItemDTO itemDTO = new ProductItemDTO();
            itemDTO.setId(id);
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
            if (images.size() > i)
                itemDTO.setImage(convertToMultipartFile((PictureData) images.get(i++)));
            itemDTO.setTags(tags);
            productItemDTOList.add(itemDTO);
        }
        return productItemDTOList;
    }

    private Object getValueByIndex(Row row, int index, Object defaultValue) {
        try {
            return row.getCell(index).getNumericCellValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private Integer getIntegerValue(Row row, int index) {
        try {
            return (int) row.getCell(index).getNumericCellValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public MultipartFile convertToMultipartFile(PictureData picture) {
        String filename = "image." + picture.suggestFileExtension();
        return new PictureDataMultipartFile(picture, filename);
    }
}
