package com.api.arv.model.dto.request.product;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class ProductItemDTO {
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
