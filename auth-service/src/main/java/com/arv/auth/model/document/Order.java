package com.arv.auth.model.document;

import com.arv.auth.model.dto.request.cart.CartDto;
import com.arv.auth.model.enums.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("order")
public class Order {
    @Id
    private String id;
    @Indexed
    private String paymentId;
    @NonNull
    private String userId;
    @NonNull
    private List<CartDto> orderItems;
    private PaymentStatus paymentStatus;
    @Indexed
    private OrderStatus orderStatus = OrderStatus.WAITING;
    @Indexed
    private ReturnStatus returnStatus;
    @Indexed
    private CancellationStatus cancellationStatus;
    @Indexed
    private PaymentMode paymentMode = PaymentMode.WALLET;
    @Indexed
    private String cancellationReason;
    @Indexed
    private Boolean isCancelledOrder = false;
    @Indexed
    private Boolean isReturnOrder = false;
    private Date orderedDate;
    private Date placedDate;
    private Date expectedDeliveryDate;
    private Date deliveredDate;
    private Double orderValue;
    private Double deliveryCharge;
    private Double taxPercentage;
    private Double totalAmount;
    @JsonIgnore
    private String addressId;
    @JsonIgnore
    private String accessToken;
    private Address deliveryAddress;
}
