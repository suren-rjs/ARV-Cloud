package com.arv.userservice.model.dto.request.wishlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistProductDTO {
    @NotNull
    private String productId;
}
