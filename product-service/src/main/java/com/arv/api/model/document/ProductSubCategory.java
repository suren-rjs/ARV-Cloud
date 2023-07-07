package com.arv.api.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("product-sub-categories")
public class ProductSubCategory {
    @Id
    private String id;
    @NotNull
    @Indexed
    private String categoryId;
    @NotNull
    private String name;
}
