package com.arv.api.model.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProductCategoryDTO {
    private String id;
    @NotNull
    private String name;
    private String image;
}
