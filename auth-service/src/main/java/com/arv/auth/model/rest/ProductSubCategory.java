package com.arv.auth.model.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSubCategory {
    private String id;
    private String categoryId;
    private String name;
}