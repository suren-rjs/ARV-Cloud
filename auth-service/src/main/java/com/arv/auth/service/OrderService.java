package com.arv.auth.service;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.Address;
import com.arv.auth.model.document.Order;
import com.arv.auth.model.dto.request.cart.CartDto;
import com.arv.auth.model.dto.request.cart.PreserveStock;
import com.arv.auth.model.dto.request.order.OrderDTO;
import com.arv.auth.model.dto.request.order.OrderList;
import com.arv.auth.model.dto.request.order.Orders;
import com.arv.auth.model.dto.response.OrderStatement;
import com.arv.auth.model.dto.response.ResponseMessage;
import com.arv.auth.model.enums.OrderStatus;
import com.arv.auth.model.enums.PaymentMode;
import com.arv.auth.model.enums.ReturnStatus;
import com.arv.auth.repository.crud.AddressRepository;
import com.arv.auth.repository.crud.OrderRepository;
import com.arv.auth.repository.data.OrderDtoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Date;
import java.util.List;

@Service
public class OrderService implements OrderDtoRepository {

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected GlobalCommonService globalCommonService;

    @Autowired
    protected AddressRepository addressRepository;

    @Override
    public OrderList findAllByUserId(int pageNumber) {
        String userId = globalCommonService.getCurrentUser().getId();
        Pageable pageable = PageRequest.of(pageNumber, 15);
        Page<Order> page = orderRepository.findAllByUserId(userId, pageable);
        List<Order> list = page.getContent();
        return new OrderList(list, pageNumber, page.getTotalElements(), globalCommonService.getTotalPage(page.getTotalPages()));
    }

    @Override
    public OrderList findAll(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 15);
        Page<Order> page = orderRepository.findAll(pageable);
        List<Order> list = page.getContent();
        return new OrderList(list, pageNumber, page.getTotalElements(), globalCommonService.getTotalPage(page.getTotalPages()));
    }

    @Override
    public Boolean isExistsById(String id) {
        String userId = globalCommonService.getCurrentUser().getId();
        return orderRepository.existsByIdAndUserId(id, userId);
    }

    @Override
    public Order findOneById(String id) {
        if (!isExistsById(id)) throw new RuntimeException("Invalid id");
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Invalid id"));
    }

    @Override
    public void newOrder(OrderDTO orderDTO) {
        String userId = globalCommonService.getCurrentUser().getId();
        Address deliveryAddress = addressRepository.findOneByIdAndUserId(orderDTO.getAddressId(), userId).orElseThrow(() -> new NotFoundException("Invalid address id"));
        Order order = (Order) globalCommonService.convertObject(orderDTO, Order.class);
        order.setDeliveryAddress(deliveryAddress);
        order.setUserId(userId);
        order.setOrderedDate(new Date());
        try {
            Order preservedOrder = preserveOrder(order, orderDTO.getAccessToken());
            orderRepository.save(preservedOrder);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Order preserveOrder(Order order, String accessToken) {
        int size = order.getOrderItems().size(), maxIndex = 0;
        boolean isFailure = false;
        for (int i = 0; i < size; i++) {
            CartDto cart = order.getOrderItems().get(i);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            // Set the request body
            ObjectMapper mapper = new ObjectMapper();
            String requestJson;
            try {
                requestJson = mapper.writeValueAsString(new PreserveStock(cart.getProductId(), cart.getQty()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            ResponseMessage message = (ResponseMessage) globalCommonService.postForObject("http://192.168.2.3:8080/products/preserve", entity, ResponseMessage.class);
            if (message.getMessage().equals("Stock preserved for order!")) {
                maxIndex++;
            } else {
                isFailure = true;
                break;
            }
        }

        if (isFailure) {
            extractInFailure(order, accessToken, maxIndex);
            throw new RuntimeException("Unable to process your order!");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        // Set the request body
        ObjectMapper mapper = new ObjectMapper();
        String requestJson;
        Orders orders = new Orders(order.getOrderItems());
        try {
            requestJson = mapper.writeValueAsString(orders);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        OrderStatement orderStatement = (OrderStatement) globalCommonService.postForObject("http://192.168.2.3:8080/public/getStatment", entity, OrderStatement.class);
        order.setTotalAmount(orderStatement.getTotalAmount());
        return order;
    }

    private void extractInFailure(Order order, String token, int maxIndex) {
        for (int i = 0; i < maxIndex; i++) {
            CartDto cart = order.getOrderItems().get(i);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            // Set the request body
            ObjectMapper mapper = new ObjectMapper();
            String requestJson;
            try {
                requestJson = mapper.writeValueAsString(new PreserveStock(cart.getProductId(), cart.getQty()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
            globalCommonService.postForObject("http://192.168.2.3:8080/products/preserve", entity, ResponseMessage.class);
        }
    }

    @Override
    public void requestReturn(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Invalid id"));

        if (order.getReturnStatus() != ReturnStatus.REQUEST_RETURN) {
            order.setReturnStatus(ReturnStatus.REQUEST_RETURN);
            orderRepository.save(order);
        }
    }

    @Override
    public void cancelOrder(String orderId) {
        if (!isExistsById(orderId)) throw new RuntimeException("Invalid id");
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Invalid id"));
        order.setOrderStatus(OrderStatus.ORDER_PLACED);
        order.setIsCancelledOrder(true);
        orderRepository.save(order);
    }

    @Override
    public void placeOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Invalid id"));
        order.setOrderStatus(OrderStatus.ORDER_PLACED);
        order.setPlacedDate(new Date());
        orderRepository.save(order);
    }

    @Override
    public void outForDelivery(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Invalid id"));
        order.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
        orderRepository.save(order);
    }

    @Override
    public void completeDelivery(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Invalid id"));
        order.setOrderStatus(OrderStatus.DELIVERED);
        order.setOrderedDate(new Date());
        orderRepository.save(order);
    }

    @Override
    public void changeReturnStatus(String id, ReturnStatus returnStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invalid id"));
        order.setReturnStatus(returnStatus);
        orderRepository.save(order);
    }

    @Override
    public void changePaymentMode(String id, PaymentMode paymentMode) {
        if (!isExistsById(id)) throw new RuntimeException("Invalid id");
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invalid id"));
        order.setPaymentMode(paymentMode);
        orderRepository.save(order);
    }

    @Override
    public void updateDeliveryAddress(String id, String addressId) {
        String userId = globalCommonService.getCurrentUser().getId();
        Address deliveryAddress = addressRepository.findOneByIdAndUserId(addressId, userId).orElseThrow(() -> new NotFoundException("Invalid id"));
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invalid id"));
        order.setDeliveryAddress(deliveryAddress);
        orderRepository.save(order);
    }
}
