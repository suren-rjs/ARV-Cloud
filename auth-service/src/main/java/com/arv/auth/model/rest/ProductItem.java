package com.arv.auth.model.rest;

import com.arv.auth.model.dto.request.cart.ProductVariant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem {
    @NotNull
    private String storeId;
    @Indexed
    private String majorCategoryId;

    @Field("Id")
    private String id;

    @NotNull
    private String productName;

    private String description;
    private String scannerId;
    private String brand;

    @NotNull
    @Field("categoryId")
    private String categoryNameOrId;

    @Field("subCategoryId")
    private String subCategoryNameOrId;

    private String subCategoryTags;

    private ProductCategory productCategory;

    private ProductSubCategory productSubCategory;

    private String[] productVariation;
    private Double[] mrpPrice;
    private Double[] sellingPrice;
    private Integer[] stock;
    private Double discount;
    private String tags;

    private byte[] image;
    private MultipartFile imageFile;
    private Date createdDate;
    @Field("productVariants")
    private ArrayList<ProductVariant> productVariants;
    private String[] color;
    private String[] size;
    private Integer orderQty;
    private String orderProductVariation;
    private Double orderPrice;
}