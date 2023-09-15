package com.arv.auth.model.dto.request.order;

import com.arv.auth.model.dto.request.cart.CartDto;
import com.arv.auth.model.enums.*;
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
    private String paymentId;
    private PaymentMode paymentMode = PaymentMode.COD;
    private PaymentStatus paymentStatus = PaymentStatus.WAITING_FOR_PAYMENT;
    private OrderStatus orderStatus = OrderStatus.WAITING;
    private ReturnStatus returnStatus;
    private CancellationStatus cancellationStatus = CancellationStatus.NOT_CANCELLED;
    private String cancellationReason;
    private Boolean isCancelledOrder = false;
    private Boolean isReturnOrder = false;
    private String addressId;
    @NonNull
    private String accessToken;
    private Timestamp orderedDate = new Timestamp(new Date().getTime());
    private Date placedDate;
    private Date expectedDeliveryDate;
    private Date deliveredDate;
    private Double orderValue;
    private Double deliveryCharge;
    private Double taxPercentage;
    private Double totalAmount;
    private String couponCode;
    private Double discountedPrice;
    private Double couponDiscountPercentage;
    private Double finalPrice;
}