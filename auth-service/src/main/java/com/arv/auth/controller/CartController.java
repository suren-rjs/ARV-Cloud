package com.arv.auth.controller;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.Cart;
import com.arv.auth.model.dto.request.cart.CartDto;
import com.arv.auth.model.dto.request.cart.ProductVariant;
import com.arv.auth.model.dto.response.CartItemsList;
import com.arv.auth.model.dto.response.Count;
import com.arv.auth.model.dto.response.Total;
import com.arv.auth.model.rest.ProductItem;
import com.arv.auth.model.rest.ProductList;
import com.arv.auth.model.rest.Products;
import com.arv.auth.service.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartController {

    @Value("${gateway.url}")
    private String gatewayUrl;

    @Autowired
    private CartService cartService;

    @Autowired
    private GlobalCommonService globalCommonService;

    @GetMapping
    private ResponseEntity<?> getCartItems(@RequestParam(required = false, defaultValue = "0") int pageNumber) throws JsonProcessingException {
        List<Cart> cartList = cartService.findAll();
        String[] productIds = cartList.stream().map(Cart::getProductId).toArray(String[]::new);

        String uriWithParams = gatewayUrl + "/public/list?page=" + pageNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Set the request body
        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(new Products(productIds));
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ProductList productList = (ProductList) globalCommonService.postForObject(uriWithParams, entity, ProductList.class);
        productList.getProducts().forEach(productItem -> {
            Integer orderQty = cartList.stream()
                    .filter(cart -> cart.getProductId().equals(productItem.getId()))
                    .findFirst()
                    .map(Cart::getQty)
                    .orElse(0);
            String orderVariant = cartList.stream()
                    .filter(cart -> cart.getProductId().equals(productItem.getId()))
                    .findFirst()
                    .map(Cart::getVariant)
                    .orElse("");
            ProductVariant productVariant = productItem.getProductVariants().stream().filter(productVariant1 -> productVariant1.getProductVariant().equals(orderVariant)).findFirst().orElse(null);
            productItem.setOrderQty(orderQty);
            productItem.setOrderProductVariation(orderVariant);
            if (productVariant != null) {
                productItem.setOrderPrice(productVariant.getPrice());
            }
        });
        return globalCommonService.getResponseOfObject(Optional.of(productList), HttpStatus.OK);
    }

    @GetMapping("/cartValue")
    private ResponseEntity<?> getCartValue() throws JsonProcessingException {
        List<Cart> cartList = cartService.findAll();
        String[] productIds = cartList.stream().map(Cart::getProductId).toArray(String[]::new);
        String[] variants = cartList.stream().map(Cart::getVariant).toArray(String[]::new);

        String uriWithParams = gatewayUrl + "/public/listAll";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Set the request body
        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(new Products(productIds));
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ProductItem[] productList = (ProductItem[]) globalCommonService.postForObject(uriWithParams, entity, ProductItem[].class);

        Map<String, Integer> productQuantityMap = new HashMap<>();

        cartList.forEach(cartDto -> {
            String productId = cartDto.getProductId();
            int quantity = cartDto.getQty();
            productQuantityMap.put(productId, productQuantityMap.getOrDefault(productId, 0) + quantity);
        });

        double totalAmount = 0.0;
        int i = 0;
        for (ProductItem productItem : productList) {
            String productId = productItem.getId();
            int variantIndex = globalCommonService.findIndex(productItem.getProductVariation(), variants[i++]);
            if (variantIndex == -1) continue;
            Double sellingPriceByVariant = productItem.getSellingPrice()[variantIndex];
            int quantity = productQuantityMap.getOrDefault(productId, 0);
            totalAmount += quantity * sellingPriceByVariant;
        }

        return globalCommonService.getResponseOfObject(Optional.of(new Total(0.0, totalAmount)), HttpStatus.OK);
    }

    @GetMapping("/idAndCount")
    private ResponseEntity<?> getCartCount() {
        CartItemsList cartItemsList = cartService.findAllAndCount();
        return globalCommonService.getResponseOfObject(Optional.of(cartItemsList), HttpStatus.OK);
    }

    @GetMapping("/cartCount")
    private ResponseEntity<?> getCartItemCountById(@RequestParam(defaultValue = "") String productId) {
        Count itemCount = new Count(cartService.findCountById(productId));
        return globalCommonService.getResponseOfObject(Optional.of(itemCount), HttpStatus.OK);
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
