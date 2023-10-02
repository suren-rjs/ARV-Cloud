package com.arv.api.model.document;

import com.arv.api.model.dto.response.ProductVariant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("product-items")
public class ProductItem {

    @Id
    private String id;
    @NotNull
    private String storeId;
    @Indexed
    private String majorCategoryId;
    @NotNull
    @UniqueElements
    private String productName;
    private String description;
    private String scannerId;
    @Indexed
    private String brand;
    @NotNull
    @Indexed
    private String categoryId;
    @Indexed
    private String subCategoryId;
    @DBRef
    private ProductCategory productCategory;
    @Indexed
    private String subCategoryTags;
    @DBRef
    private ProductSubCategory productSubCategory;
    @Indexed
    private String[] productVariation;
    @Indexed
    private Double[] mrpPrice;
    @Indexed
    private Double[] sellingPrice;
    @Indexed
    private Integer[] stock;
    @Indexed
    private Double discount;
    private String tags;
    private String imageUri;
    @JsonIgnore
    private MultipartFile imageFile;
    @JsonIgnore
    private String categoryNameOrId;
    @JsonIgnore
    private String subCategoryNameOrId;
    private Date createdDate = new Date();
    @Field("productVariants")
    private ArrayList<ProductVariant> productVariants;
    @Indexed
    private String[] color;
    @Indexed
    private String[] size;
    private Boolean isEnabled;

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
