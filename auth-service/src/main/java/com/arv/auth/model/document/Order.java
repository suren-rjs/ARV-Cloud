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
