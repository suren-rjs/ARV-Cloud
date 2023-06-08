package com.api.arv.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
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
    private String categoryId;
    private String subCategoryId;
    private String subSubCategoryId;
    private String productVariation;
    private Double mrpPrice;
    private Double sellingPrice;
    private Integer quantity;
    private Integer stock;
    private Double discount;
}
