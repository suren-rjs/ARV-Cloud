package com.arv.auth.model.document;

import com.arv.auth.model.enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
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
    private List<Cart> orderItems;
    private PaymentStatus paymentStatus;
    private OrderStatus orderStatus;
    private ReturnStatus returnStatus;
    private CancellationStatus cancellationStatus;
    private PaymentMode paymentMode = PaymentMode.WALLET;
    private String cancellationReason;
    private Boolean isCancelledOrder = false;
    private Boolean isReturnOrder = false;
    private Timestamp orderedDate = new Timestamp(new Date().getTime());
    private Timestamp placedDate;
    private Timestamp expectedDeliveryDate;
    private Timestamp deliveredDate;
    private double orderValue;
    private double deliveryCharge;
    private double taxPercentage;
    private double totalAmount;
    private Address deliveryAddress;
}
