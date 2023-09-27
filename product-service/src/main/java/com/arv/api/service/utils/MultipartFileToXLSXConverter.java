package com.arv.api.service.utils;

import com.arv.api.helper.PictureDataMultipartFile;
import com.arv.api.model.dto.request.ProductItemDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MultipartFileToXLSXConverter {


    public List<ProductItemDTO> convert(MultipartFile multipartFile, String storeId, String majorCategoryId) throws Exception {
        try (InputStream inputStream = multipartFile.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            String outputFilePath = "uploaded-file.xlsx";

            try (Workbook outputWorkbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.getSheetAt(0);
                List<XSSFPicture> images = new ArrayList<>();
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet inputSheet = workbook.getSheetAt(i);
                    Sheet outputSheet = outputWorkbook.createSheet(inputSheet.getSheetName());
                    copySheet(inputSheet, outputSheet);
                }

                XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
                for (XSSFShape shape : drawing.getShapes()) {
                    if (shape instanceof XSSFPicture) {
                        XSSFPicture picture = (XSSFPicture) shape;
                        XSSFClientAnchor anchor = picture.getClientAnchor();
                        int imageIndex = Arrays.asList("Vegetables", "Food", "CHAAT").contains(majorCategoryId) ? 11 : 13;
                        if (anchor.getRow1() != 0 && anchor.getCol1() == imageIndex) {
                            images.add(picture);
                        } else {
                            images.add(null);
                        }
                    }
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
    private List<ProductItemDTO> getProductItemDTOList(Optional<Workbook> uploadedData, List<XSSFPicture> images, String storeId, String majorCategoryId) throws Exception {
        if (!uploadedData.isPresent()) {
            throw new Exception("Unable to upload");
        }

        Sheet sheet = uploadedData.get().getSheetAt(0);
        List<ProductItemDTO> productItemDTOList = new ArrayList<>();
        int i = 0;
        if (majorCategoryId.equals("Groceries")) {
            for (Row row : sheet) {
                if (row.getCell(0).getCellType() == CellType.BLANK || row.getRowNum() == 0) continue;
                String name = row.getCell(0).getStringCellValue();
                String scannerId = (String) getValueByIndex(row, 1, true);
                String brand = getStringValueByIndex(row, 2, "-");
                String description = getStringValueByIndex(row, 3, "-");
                String category = row.getCell(5).getStringCellValue();
                String categoryTag = getStringValueByIndex(row, 6, category);
                String subCategory = getStringValueByIndex(row, 6, category);

                String[] productVariation;
                try {
                    productVariation = getStringValueByIndex(row, 10, "-").split(",");
                } catch (Exception ignored) {
                    productVariation = new String[]{};
                }

                Double[] mrpPrice = getDoubleListByIndex(row, 7);
                Double[] sellingPrice = getDoubleListByIndex(row, 8);

                Double discount = (Double) getValueByIndex(row, 9, false);

                Integer[] stock = getIntListByIndex(row, 11);
                String id = row.getCell(0).getStringCellValue().toLowerCase().replaceAll(" ", "-");
                String tags = "";
                try {
                    tags = row.getCell(14).getStringCellValue().toLowerCase();
                } catch (Exception ignored) {

                }

                ProductItemDTO itemDTO = new ProductItemDTO();
                itemDTO.setStoreId(storeId);
                itemDTO.setScannerId(scannerId);
                itemDTO.setBrand(brand);
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
                if (images.size() > i) {
                    itemDTO.setImageFile(convertXSSFPictureToMultipartFile(images.get(i++)));
                }
                itemDTO.setTags(tags);
                productItemDTOList.add(itemDTO);
            }
        } else if (Arrays.asList("Fashion", "Fancy").contains(majorCategoryId)) {
            for (Row row : sheet) {
                if (row.getCell(0).getCellType() == CellType.BLANK || row.getRowNum() == 0) continue;
                String name = row.getCell(0).getStringCellValue();
                String brand = getStringValueByIndex(row, 1, "-");
                String description = getStringValueByIndex(row, 2, "-");
                String category = row.getCell(4).getStringCellValue();
                String categoryTag = getStringValueByIndex(row, 5, category);
                String subCategory = getStringValueByIndex(row, 5, category);

                String[] size;
                String[] color;

                Double[] mrpPrice = getDoubleListByIndex(row, 6);
                Double[] sellingPrice = getDoubleListByIndex(row, 7);

                Double discount = (Double) getValueByIndex(row, 8, false);

                try {
                    color = getStringValueByIndex(row, 9, "-").split(",");
                } catch (Exception ignored) {
                    color = new String[]{};
                }

                try {
                    size = getStringValueByIndex(row, 10, "-").split(",");
                } catch (Exception ignored) {
                    size = new String[]{};
                }

                Integer[] stock = getIntListByIndex(row, 11);
                String scannerId = (String) getValueByIndex(row, 12, true);
                String id = row.getCell(0).getStringCellValue().toLowerCase().replaceAll(" ", "-");
                String tags = "";
                try {
                    tags = row.getCell(14).getStringCellValue().toLowerCase();
                } catch (Exception ignored) {

                }

                ProductItemDTO itemDTO = new ProductItemDTO();
                itemDTO.setStoreId(storeId);
                itemDTO.setScannerId(scannerId);
                itemDTO.setBrand(brand);
                itemDTO.setId(storeId + id);
                itemDTO.setProductName(name);
                itemDTO.setDescription(description);
                itemDTO.setCategoryNameOrId(category);
                itemDTO.setSubCategoryTags(categoryTag);
                itemDTO.setSubCategoryNameOrId(subCategory);
                itemDTO.setMrpPrice(mrpPrice);
                itemDTO.setSellingPrice(sellingPrice);
                itemDTO.setDiscount(discount);
                itemDTO.setProductVariation(size);
                itemDTO.setStock(stock);
                itemDTO.setMajorCategoryId(majorCategoryId);
                itemDTO.setColor(color);
                itemDTO.setColor(size);
                if (images.size() > i) {
                    itemDTO.setImageFile(convertXSSFPictureToMultipartFile(images.get(i++)));
                }
                itemDTO.setTags(tags);
                productItemDTOList.add(itemDTO);
            }
        } else if (Arrays.asList("Vegetables", "Food", "CHAAT").contains(majorCategoryId)) {
            for (Row row : sheet) {
                if (row.getCell(0).getCellType() == CellType.BLANK || row.getRowNum() == 0) continue;
                String name = row.getCell(0).getStringCellValue();
                String description = getStringValueByIndex(row, 1, "-");
                String scannerId = "-";
                String brand = "-";
                String category = row.getCell(3).getStringCellValue();
                String categoryTag = getStringValueByIndex(row, 4, category);
                String subCategory = getStringValueByIndex(row, 4, category);

                Double[] mrpPrice = getDoubleListByIndex(row, 5);
                Double[] sellingPrice = getDoubleListByIndex(row, 6);
                Double discount = (Double) getValueByIndex(row, 7, false);

                String[] productVariation;
                try {
                    productVariation = getStringValueByIndex(row, 8, "-").split(",");
                } catch (Exception ignored) {
                    productVariation = new String[]{};
                }

                Integer[] stock = getIntListByIndex(row, 9);
                String id = row.getCell(0).getStringCellValue().toLowerCase().replaceAll(" ", "-");
                String tags = "";
                try {
                    tags = row.getCell(11).getStringCellValue().toLowerCase();
                } catch (Exception ignored) {

                }

                ProductItemDTO itemDTO = new ProductItemDTO();
                itemDTO.setStoreId(storeId);
                itemDTO.setScannerId(scannerId);
                itemDTO.setBrand(brand);
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
                if (images.size() > i) {
                    itemDTO.setImageFile(convertXSSFPictureToMultipartFile(images.get(i++)));
                }
                itemDTO.setTags(tags);
                productItemDTOList.add(itemDTO);
            }
        }
        return productItemDTOList;
    }

    private Double[] getDoubleListByIndex(Row row, int index) {
        try {
            if (row.getCell(index).getCellType() == CellType.NUMERIC) {
                Double value = row.getCell(index).getNumericCellValue();
                row.getCell(index).setCellValue(value + ",");
            }
            String[] price = row.getCell(index).getStringCellValue().split(",");
            return Arrays.stream(price).map(Double::parseDouble).toArray(Double[]::new);
        } catch (Exception e) {
            return new Double[]{};
        }
    }

    private Integer[] getIntListByIndex(Row row, int index) {
        try {
            if (row.getCell(index).getCellType() == CellType.NUMERIC) {
                Double value = row.getCell(index).getNumericCellValue();
                row.getCell(index).setCellValue(value + ",");
            }
            String[] stock = row.getCell(index).getStringCellValue().split(",");
            return Arrays.stream(stock).map(Float::parseFloat).map(Math::round).toArray(Integer[]::new);
        } catch (Exception e) {
            return new Integer[]{};
        }
    }

    private Object getValueByIndex(Row row, int index, boolean isString) {
        Object object;
        try {
            object = row.getCell(index).getNumericCellValue();
            return isString ? object + "" : object;
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

    public MultipartFile convertToMultipartFile(PictureData picture) {
        String filename = "image." + picture.suggestFileExtension();
        return new PictureDataMultipartFile(picture, filename);
    }

    public static MultipartFile convertXSSFPictureToMultipartFile(XSSFPicture picture) throws IOException {
        if (picture == null) return null;
        byte[] imageData = picture.getPictureData().getData();

        return new ByteArrayMultipartFile("imageFile", "image.png", "image/png", imageData);
    }

}
