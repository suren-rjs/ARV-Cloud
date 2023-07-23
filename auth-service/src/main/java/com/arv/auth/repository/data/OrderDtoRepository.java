package com.arv.auth.repository.data;

import com.arv.auth.model.document.Order;
import com.arv.auth.model.dto.request.order.OrderDTO;
import com.arv.auth.model.dto.request.order.OrderList;
import com.arv.auth.model.enums.PaymentMode;
import com.arv.auth.model.enums.ReturnStatus;

public interface OrderDtoRepository {
    OrderList findAllByUserId(int page);
    OrderList findAll(int page);

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
