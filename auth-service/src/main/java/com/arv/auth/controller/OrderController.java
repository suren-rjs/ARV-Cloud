package com.arv.auth.controller;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.dto.request.order.OrderDTO;
import com.arv.auth.model.enums.*;
import com.arv.auth.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {
    @Autowired
    protected GlobalCommonService globalCommonService;
    @Autowired
    protected OrderService orderService;

    @GetMapping
    private ResponseEntity<?> getOrder(@RequestParam(required = false, defaultValue = "0") int page) {
        try {
            return globalCommonService.getResponseOfObject(Optional.of(orderService.findAllByUserId(page)), HttpStatus.OK);
        } catch (Exception e) {
            return globalCommonService.getResponseEntityByMessageAndStatus(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/ordersByPaymentStatus")
    private ResponseEntity<?> ordersByPaymentStatus(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "WAITING_FOR_PAYMENT") PaymentStatus paymentStatus,
            @RequestParam(required = false, defaultValue = "") String orderId
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(required = false, defaultValue = "") Date fromOrderedDateDate,

    ) {
        try {
//            fromOrderedDateDate = fromOrderedDateDate == null ? new Date(2010, Calendar.JANUARY, 1) : fromOrderedDateDate;
//            toOrderedDateDate = toOrderedDateDate == null ? new Date() : toOrderedDateDate;
//            if (fromOrderedDateDate.after(toOrderedDateDate)) {
//                fromOrderedDateDate = new Date(2010, Calendar.JANUARY, 1);
//            }
            return globalCommonService.getResponseOfObject(Optional.of(orderService.findByPaymentStatusAndOrderIdContaining(paymentStatus, orderId, page)), HttpStatus.OK);
        } catch (Exception e) {
            return globalCommonService.getResponseEntityByMessageAndStatus(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/ordersByOrderStatus")
    private ResponseEntity<?> ordersByOrderStatus(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "WAITING") OrderStatus orderStatus,
            @RequestParam(required = false, defaultValue = "") String orderId
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(required = false, defaultValue = "") Date fromOrderedDateDate,

    ) {
        try {
//            fromOrderedDateDate = fromOrderedDateDate == null ? new Date(2010, Calendar.JANUARY, 1) : fromOrderedDateDate;
//            toOrderedDateDate = toOrderedDateDate == null ? new Date() : toOrderedDateDate;
//            if (fromOrderedDateDate.after(toOrderedDateDate)) {
//                fromOrderedDateDate = new Date(2010, Calendar.JANUARY, 1);
//            }
            return globalCommonService.getResponseOfObject(Optional.of(orderService.findByOrderStatusAndOrderIdContaining(orderStatus, orderId, page)), HttpStatus.OK);
        } catch (Exception e) {
            return globalCommonService.getResponseEntityByMessageAndStatus(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> getOrder(@PathVariable String id) {
        try {
            return globalCommonService.getResponseOfObject(Optional.of(orderService.findOneById(id)), HttpStatus.OK);
        } catch (Exception e) {
            return globalCommonService.getResponseEntityByMessageAndStatus(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    private ResponseEntity<?> newOrder(@RequestBody OrderDTO orderDTO) {
        HttpStatus status;
        String message = "Order placement success!";
        try {
            status = HttpStatus.OK;
            orderService.newOrder(orderDTO);
        } catch (Exception e) {
            status = HttpStatus.NOT_ACCEPTABLE;
            message = e.getMessage();
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @PostMapping("/placeOrder/{id}")
    private ResponseEntity<?> placeOrder(@PathVariable String id) {
        HttpStatus status;
        String message = "Order placement success!";
        try {
            status = HttpStatus.OK;
            orderService.placeOrder(id);
        } catch (Exception e) {
            status = HttpStatus.NOT_ACCEPTABLE;
            message = "Something went wrong!, unable to place your order!";
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @PostMapping("/cancelOrder/{id}")
    private ResponseEntity<?> cancelOrder(@PathVariable String id) {
        HttpStatus status;
        String message = "Cancellation in progress!";
        try {
            status = HttpStatus.OK;
            orderService.cancelOrder(id);
        } catch (Exception e) {
            status = HttpStatus.NOT_ACCEPTABLE;
            message = "Something went wrong!, unable to cancel your order!";
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @PostMapping("/outForDelivery/{id}")
    private ResponseEntity<?> outForDelivery(@PathVariable String id) {
        HttpStatus status;
        String message = "Order status updated!";
        try {
            status = HttpStatus.OK;
            orderService.outForDelivery(id);
        } catch (Exception e) {
            status = HttpStatus.NOT_ACCEPTABLE;
            message = "Something went wrong!, unable to process order!";
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @PostMapping("/complete/{id}")
    private ResponseEntity<?> completeDelivery(@PathVariable String id) {
        HttpStatus status;
        String message = "Order status updated!";
        try {
            status = HttpStatus.OK;
            orderService.completeDelivery(id);
        } catch (Exception e) {
            status = HttpStatus.NOT_ACCEPTABLE;
            message = "Something went wrong!, unable to process order!";
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @PostMapping("/updateDeliveryAddress/{id}")
    private ResponseEntity<?> updateDeliveryAddress(@PathVariable String id, @RequestParam String addressId) {
        HttpStatus status;
        String message = "Order address updated!";
        try {
            status = HttpStatus.OK;
            orderService.updateDeliveryAddress(id, addressId);
        } catch (Exception e) {
            status = HttpStatus.NOT_ACCEPTABLE;
            message = "Something went wrong!, unable to process order!";
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @PostMapping("/return/{id}")
    private ResponseEntity<?> requestReturn(@PathVariable String id) {
        HttpStatus status;
        String message = "Return status progressing!";
        try {
            status = HttpStatus.OK;
            orderService.requestReturn(id);
        } catch (Exception e) {
            status = HttpStatus.NOT_ACCEPTABLE;
            message = "Something went wrong!, unable to process order!";
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @PostMapping("/changePaymentMode/{id}")
    private ResponseEntity<?> changePaymentMode(@PathVariable String id, @RequestParam PaymentMode paymentMode) {
        HttpStatus status;
        String message = "PaymentMode updated!";
        try {
            status = HttpStatus.OK;
            orderService.changePaymentMode(id, paymentMode);
        } catch (Exception e) {
            status = HttpStatus.NOT_ACCEPTABLE;
            message = "Something went wrong!, unable to process order!";
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @PostMapping("/cancelReturn/{id}")
    private ResponseEntity<?> changeReturnStatus(@PathVariable String id, @RequestParam ReturnStatus returnStatus) {
        HttpStatus status;
        String message = "Return status updated!";
        try {
            status = HttpStatus.OK;
            orderService.changeReturnStatus(id, returnStatus);
        } catch (Exception e) {
            status = HttpStatus.NOT_ACCEPTABLE;
            message = "Something went wrong!, unable to process order!";
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

}
