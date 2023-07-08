package com.arv.api.model.dto.request.product;

import com.arv.api.model.document.ProductCategory;
import com.arv.api.model.document.ProductSubCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class ProductItemDTO {
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
    private MultipartFile image;
}
