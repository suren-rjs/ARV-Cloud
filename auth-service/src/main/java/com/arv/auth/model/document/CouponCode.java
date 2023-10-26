package com.arv.auth.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("coupons")
public class CouponCode {
    @Id
    private String id;
    @NonNull
    @Indexed
    private String couponCode;
    @NonNull
    private Double discountPercentage = 0.0;
    @NonNull
    @Indexed
    private Double minOrderPrice = 0.0;
    @NonNull
    private Double maxDiscountPrice = 0.0;
    private Integer maxUsage = 0;
    private Integer currentUsage = 0;
}

