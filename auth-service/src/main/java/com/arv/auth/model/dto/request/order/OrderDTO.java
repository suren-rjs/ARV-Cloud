package com.arv.auth.model.dto.request.order;

import com.arv.auth.model.dto.request.cart.CartDto;
import com.arv.auth.model.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @NonNull
    private List<CartDto> orderItems;
    private PaymentMode paymentMode = PaymentMode.WALLET;
    private String addressId;
    @NonNull
    private String accessToken;
    private Timestamp orderedDate = new Timestamp(new Date().getTime());
}