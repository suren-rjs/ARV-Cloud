package com.arv.auth.model.dto.request.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariant {
    private String productVariant;
    private String color;
    private String size;
    private String unit;
    private Double price;
    private Integer qty;
}