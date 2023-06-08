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
@Document("product-sub-sub-categories")
public class ProductSubSubCategory {
    @Id
    private String id;
    @NotNull
    private String categoryId;
    @NotNull
    private String subCategoryId;
    @NotNull
    private String name;
}
