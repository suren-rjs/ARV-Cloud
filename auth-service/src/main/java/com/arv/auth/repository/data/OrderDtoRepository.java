package com.arv.auth.repository.data;

import com.arv.auth.model.document.Order;
import com.arv.auth.model.dto.request.order.OrderDTO;
import com.arv.auth.model.dto.request.order.OrderList;
import com.arv.auth.model.enums.*;

import java.util.Date;

public interface OrderDtoRepository {
    OrderList findAllByUserId(int page);

    OrderList findByPaymentStatusAndOrderIdContaining(PaymentStatus paymentStatus, String orderId, int pageNumber);

    OrderList findByOrderStatusAndOrderIdContaining(OrderStatus orderStatus, String orderId, int pageNumber);

    Boolean isExistsById(String id);

    Order findOneById(String id);

    void newOrder(OrderDTO orderDTO);

    void requestReturn(String orderId);

    void cancelOrder(String orderId);

    void placeOrder(String orderId);

    void outForDelivery(String orderId);

    void completeDelivery(String orderId);

    void changeReturnStatus(String id, ReturnStatus returnStatus);

    void changePaymentMode(String id, PaymentMode paymentMode);

    void updateDeliveryAddress(String id, String addressId);
}
