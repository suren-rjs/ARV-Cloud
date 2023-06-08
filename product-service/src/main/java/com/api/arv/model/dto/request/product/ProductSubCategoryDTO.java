package com.api.arv.model.dto.request.product;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Data
public class ProductSubCategoryDTO {
    @Id
    private String id;
    @NotNull
    private String categoryId;
    @NotNull
    private String name;
}
