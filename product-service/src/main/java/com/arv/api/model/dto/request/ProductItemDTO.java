package com.arv.api.model.dto.request;

import com.arv.api.model.document.ProductCategory;
import com.arv.api.model.document.ProductSubCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemDTO {
    @NotNull
    private String storeId;
    private String majorCategoryId;
    private String id;
    @NotNull
    private String productName;
    private String description;
    @NotNull
    private String categoryNameOrId;
    private String subCategoryNameOrId;
    private String subCategoryTags;
    @DBRef
    @JsonIgnore
    private ProductCategory productCategory;
    @DBRef
    @JsonIgnore
    private ProductSubCategory productSubCategory;
    private String productVariation;
    private Double mrpPrice;
    private Double sellingPrice;
    private Integer quantity;
    private Integer stock;
    private Double discount;
    private String tags;

    private byte[] image;
    private MultipartFile imageFile;

    public String getCategoryId() {
        return this.productCategory.getId();
    }

    public String getSubCategoryId() {
        return this.productSubCategory.getId();
    }
}
