package com.arv.auth.repository.crud;

import com.arv.auth.model.document.Order;
import com.arv.auth.model.enums.OrderStatus;
import com.arv.auth.model.enums.PaymentMode;
import com.arv.auth.model.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;

public interface OrderRepository extends MongoRepository<Order, String> {
    Page<Order> findAllByUserId(String id, Pageable pageable);

    Page<Order> findByPaymentStatusContainingOrOrderStatusContainingOrPaymentModeContaining(PaymentStatus paymentStatus, OrderStatus orderStatus, PaymentMode paymentMode, Pageable pageable);

    Boolean existsByIdAndUserId(String id, String userId);

    Page<Order> findByPaymentStatusAndOrderIdContaining(
            PaymentStatus paymentStatus,
            String orderId,
            Pageable pageable
    );

    Page<Order> findByOrderStatusAndOrderIdContaining(
            OrderStatus orderStatus,
            String orderId,
            Pageable pageable
    );

    @Query("{ 'paymentStatus' : { $regex: ?0, $options: 'i' }, 'orderId' : { $regex: ?1, $options: 'i' }, 'orderedDate': { $gte: ?2, $lte: ?3 } }")
    Page<Order> findByPaymentStatusAndOrderIdContainingAndOrderedDateBetween(
            PaymentStatus paymentStatus,
            String orderId,
            Date fromOrderDate,
            Date toOrderDate,
            Pageable pageable
    );
}
