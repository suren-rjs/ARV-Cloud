package com.arv.auth.model.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem {

    private String id;
    private String storeId;
    private String majorCategoryId;
    private String productName;
    private String description;
    private String scannerId;
    private String brand;
    private String categoryId;
    private String subCategoryId;
    private ProductCategory productCategory;
    private String subCategoryTags;
    private ProductSubCategory productSubCategory;
    private String[] productVariation;
    private Double[] mrpPrice;
    private Double[] sellingPrice;
    private Integer[] quantity;
    private Integer[] stock;
    private Double discount;
    private String tags;
    private String imageUri;
    private Integer orderQty;
    private String orderProductVariation;
}