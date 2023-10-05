package com.arv.auth.model.dto.request.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDetail {
    private String productId;
    private String variant;
    private String itemName;
    private Integer qty;
    private Double itemPrice;
    private Double itemTotalPrice;
}
