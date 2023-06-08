package com.api.arv.service.utils;

import com.api.arv.model.dto.request.product.ProductItemDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MultipartFileToXLSXConverter {
    public List<ProductItemDTO> convert(MultipartFile multipartFile) throws Exception {
        try (InputStream inputStream = multipartFile.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            String outputFilePath = "uploaded-file.xlsx";
            try (Workbook outputWorkbook = new XSSFWorkbook()) {
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet inputSheet = workbook.getSheetAt(i);
                    Sheet outputSheet = outputWorkbook.createSheet(inputSheet.getSheetName());
                    copySheet(inputSheet, outputSheet);
                }

                try (OutputStream outputStream = new FileOutputStream(outputFilePath)) {
                    outputWorkbook.write(outputStream);
                    return getProductItemDTOList(Optional.of(outputWorkbook));
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
    private List<ProductItemDTO> getProductItemDTOList(Optional<Workbook> uploadedData) throws Exception {
        if (uploadedData.isEmpty()) {
            throw new Exception("Unable to upload");
        }

        Sheet sheet = uploadedData.get().getSheetAt(0);
        List<ProductItemDTO> productItemDTOList = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getCell(0) == null || row.getRowNum() == 0) continue;
            String id = row.getCell(0).getStringCellValue();
            String name = row.getCell(1).getStringCellValue();
            String description = (String) getValueByIndex(row, 2, "-");
            String category = row.getCell(3).getStringCellValue();
            String subCategory = getValueByIndex(row, 4, null) == null ? category : row.getCell(4).getStringCellValue();
            String subSubCategory = getValueByIndex(row, 5, null) == null ? subCategory : row.getCell(5).getStringCellValue();
            Double mrpPrice = (Double) getValueByIndex(row, 6, (double) 0);
            Double sellingPrice = (Double) getValueByIndex(row, 7, (double) 0);
            Double discount = (Double) getValueByIndex(row, 8, (double) 0);
            String productVariation = (String) getValueByIndex(row, 9, "-");
            Integer stock = getIntegerValue(row, 10);

            ProductItemDTO itemDTO = new ProductItemDTO();
            itemDTO.setId(id);
            itemDTO.setProductName(name);
            itemDTO.setDescription(description);
            itemDTO.setCategoryId(category);
            itemDTO.setSubCategoryId(subCategory);
            itemDTO.setSubSubCategoryId(subSubCategory);
            itemDTO.setMrpPrice(mrpPrice);
            itemDTO.setSellingPrice(sellingPrice);
            itemDTO.setDiscount(discount);
            itemDTO.setProductVariation(productVariation);
            itemDTO.setStock(stock);

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
}
