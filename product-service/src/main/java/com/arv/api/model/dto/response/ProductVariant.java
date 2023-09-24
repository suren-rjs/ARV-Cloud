package com.arv.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariant {
    private String productVariant;
    private Double price;
    private Integer qty;
}
