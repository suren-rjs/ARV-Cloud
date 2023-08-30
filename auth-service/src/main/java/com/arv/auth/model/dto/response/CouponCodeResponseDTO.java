package com.arv.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponCodeResponseDTO {
    Double discountedPrice;
    Boolean isValidCoupon;
    Double finalPrice;
}
