package com.arv.auth.controller;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.dto.request.cart.CartDto;
import com.arv.auth.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private GlobalCommonService globalCommonService;

    @GetMapping
    private ResponseEntity<?> getCartItems() {
        return globalCommonService.getResponseOfObject(Optional.of(cartService.findAll()), HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<?> addToCart(@RequestBody CartDto cartDto) {
        String message = "Product added to cart";
        HttpStatus status = HttpStatus.OK;
        try {
            if (Objects.equals(cartDto.getProductId(), "")) throw new RuntimeException("Invalid product id");
            cartService.save(cartDto);
        } catch (Exception e) {
            message = e.getMessage();
            status = HttpStatus.NOT_ACCEPTABLE;
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @DeleteMapping
    private ResponseEntity<?> deleteFromCart(String id) {
        String message = "Product has been removed from cart";
        HttpStatus status = HttpStatus.OK;
        try {
            if (Objects.equals(id, "")) throw new RuntimeException("Invalid product id");
            cartService.delete(id);
        } catch (Exception e) {
            message = e.getMessage();
            status = HttpStatus.NOT_ACCEPTABLE;
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @DeleteMapping("/deleteAll")
    private ResponseEntity<?> deleteAllFromCart() {
        String message = "All products are removed from cart!";
        HttpStatus status = HttpStatus.OK;
        cartService.deleteAll();
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }
}
