package com.arv.api.model.dto.response;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariant {
    private String productVariant;
    @Nullable
    private String color;
    @Nullable
    private String size;
    private Double price;
    private Integer qty;
}
