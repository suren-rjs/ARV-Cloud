package com.arv.auth.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponCodeDTO {
    private String id;
    @NonNull
    private String couponCode;
    @NonNull
    private Double discountPercentage;
    @NonNull
    private Double minOrderPrice;
    @NonNull
    private Double maxDiscountPrice;
    private int maxUsage;
}
