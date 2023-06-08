package com.api.arv.model.dto.request.product;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProductCategoryDTO {
    private String id;
    @NotNull
    private String name;
    private String image;
}
