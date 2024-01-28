package com.arv.api.model.dto.request;

import com.arv.api.model.document.ProductCategory;
import com.arv.api.model.document.ProductSubCategory;
import com.arv.api.model.dto.response.ProductVariant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemDTO {
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

    @DBRef
    @JsonIgnore
    private ProductCategory productCategory;

    @DBRef
    @JsonIgnore
    private ProductSubCategory productSubCategory;

    private String[] productVariation;
    private Double[] mrpPrice;
    private Double[] sellingPrice;
    private Integer[] stock;
    private Double[] vDiscount;
    private Double discount;
    private String tags;

    private byte[] image;
    private MultipartFile imageFile;
    private Date createdDate = new Date();
    @Field("productVariants")
    private ArrayList<ProductVariant> productVariants;
    private String[] color;
    private String[] size;

    public String getCategoryId() {
        return this.productCategory.getId();
    }

    public String getSubCategoryId() {
        return this.productSubCategory.getId();
    }


    public ArrayList<ProductVariant> getProductVariants() {
        String[] variants = this.getProductVariation();
        Double[] prices = this.getSellingPrice();
        Integer[] quantities = this.getStock();
        String[] color = this.getColor();
        String[] size = this.getSize();
        String unit = this.majorCategoryId.equals("Vegetables") ? "Kg" : "qty";
        ArrayList<ProductVariant> variantsList = new ArrayList<>();
        for (int i = 0; i < variants.length; i++) {
            String variant = variants[i];
            String variantColor;
            String variantSize;
            Double price;
            Integer quantity;
            try {
                price = prices[i];
            } catch (Exception e) {
                price = 0.0;
            }
            try {
                quantity = quantities[i];
            } catch (Exception e) {
                quantity = 0;
            }
            try {
                variantColor = color[i];
            } catch (Exception e) {
                variantColor = null;
            }
            try {
                variantSize = size[i];
            } catch (Exception e) {
                variantSize = null;
            }
            variantsList.add(new ProductVariant(variant, variantColor, variantSize, unit, price, quantity));
        }
        return variantsList;
    }
}
