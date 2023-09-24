package com.arv.auth.model.dto.request.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private String productId;
    private String variant;
    private Integer qty;
    @JsonIgnore
    private Double orderPrice;
}
