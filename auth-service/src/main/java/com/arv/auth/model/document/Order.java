package com.arv.auth.model.document;

import com.arv.auth.model.dto.request.order.OrderItemDetail;
import com.arv.auth.model.enums.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("order")
public class Order {
    @Id
    private String id;
    @JsonIgnore
    private String orderId;
    @Indexed
    private String paymentId;
    @NonNull
    private String userId;
    @NonNull
    private List<OrderItemDetail> orderItems;
    private PaymentStatus paymentStatus = PaymentStatus.WAITING_FOR_PAYMENT;
    @Indexed
    private OrderStatus orderStatus = OrderStatus.WAITING;
    @Indexed
    private ReturnStatus returnStatus;
    @Indexed
    private CancellationStatus cancellationStatus;
    @Indexed
    private PaymentMode paymentMode = PaymentMode.COD;
    @Indexed
    private String cancellationReason;
    @Indexed
    private Boolean isCancelledOrder = false;
    @Indexed
    private Boolean isReturnOrder = false;
    @Field(targetType = FieldType.STRING)
    private Date orderedDate;
    @Field(targetType = FieldType.STRING)
    private Date placedDate;
    @Field(targetType = FieldType.STRING)
    private Date expectedDeliveryDate;
    @Field(targetType = FieldType.STRING)
    private Date deliveredDate;
    private Double orderValue;
    private Double deliveryCharge;
    private Double taxPercentage;
    private Double totalAmount;
    private Double deliveryBoyTip;
    @JsonIgnore
    private String addressId;
    @JsonIgnore
    private String accessToken;
    private Address deliveryAddress;
    private String couponCode;
    private Double discountedPrice;
    private Double couponDiscountPercentage;
    private Double finalPrice;
    private Employee deliveryMan;
}
