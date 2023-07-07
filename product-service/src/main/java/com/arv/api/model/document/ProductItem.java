package com.arv.api.model.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("product-items")
public class ProductItem {

    @Id
    private String id;
    @NotNull
    private String productName;
    private String description;
    @NotNull
    @Indexed
    @JsonIgnore
    private String categoryId;
    @Indexed
    @JsonIgnore
    private String subSubCategoryId;
    @DBRef
    private ProductCategory productCategory;
    @Indexed
    private String subCategoryTags;
    @DBRef
    private ProductSubCategory productSubCategory;
    @Indexed
    private String productVariation;
    @Indexed
    private Double mrpPrice;
    @Indexed
    private Double sellingPrice;
    private Integer quantity;
    @Indexed
    private Integer stock;
    @Indexed
    private Double discount;
    private String tags;
    private String image;
}
