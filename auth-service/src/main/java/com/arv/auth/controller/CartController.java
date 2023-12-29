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
        List<ProductItem> orderCartList = new ArrayList<>();
        cartList.forEach(cart -> {
            ProductItem productItem = productList.getProducts().stream().filter(product -> product.getId().equals(cart.getProductId())).findFirst().orElse(null);

            if (productItem != null) {
                ProductItem orderedItem = new ProductItem(
                        productItem.getStoreId(),
                        productItem.getMajorCategoryId(),
                        productItem.getId(),
                        productItem.getProductName(),
                        productItem.getDescription(),
                        productItem.getScannerId(),
                        productItem.getBrand(),
                        productItem.getCategoryNameOrId(),
                        productItem.getSubCategoryNameOrId(),
                        productItem.getSubCategoryTags(),
                        productItem.getProductCategory(),
                        productItem.getProductSubCategory(),
                        productItem.getProductVariation(),
                        productItem.getMrpPrice(),
                        productItem.getSellingPrice(),
                        productItem.getStock(),
                        productItem.getDiscount(),
                        productItem.getTags(),
                        productItem.getImage(),
                        productItem.getImageFile(),
                        productItem.getCreatedDate(),
                        productItem.getProductVariants(),
                        productItem.getColor(),
                        productItem.getSize(),
                        productItem.getOrderQty(),
                        productItem.getOrderProductVariation(),
                        productItem.getOrderPrice()
                );
                orderedItem.setOrderProductVariation(cart.getVariant());
                orderedItem.setOrderQty(cart.getQty());
                ProductVariant productVariant = orderedItem.getProductVariants().stream().filter(productVariant1 -> productVariant1.getProductVariant().equals(cart.getVariant())).findFirst().orElse(null);
                if (productVariant != null) {
                    orderedItem.setOrderPrice(productVariant.getPrice());
                }
                orderCartList.add(orderedItem);
            }
        });
        ProductList orderedProductList = new ProductList(orderCartList, 0, orderCartList.size(), 0);

        return globalCommonService.getResponseOfObject(Optional.of(orderedProductList), HttpStatus.OK);
    }

    @GetMapping("/cartValue")
    private ResponseEntity<?> getCartValue() throws JsonProcessingException {
        List<Cart> cartList = cartService.findAll();
        Double[] totalPrice = cartList.stream().map(Cart::getOrderPrice).toArray(Double[]::new);
        double totalAmount = 0.0;
        for (Double price : totalPrice) {
            if(price == null) price = 0.0;
            totalAmount += price;
        }

        return globalCommonService.getResponseOfObject(Optional.of(new Total(0.0, totalAmount)), HttpStatus.OK);
    }

    @GetMapping("/idAndCount")
    private ResponseEntity<?> getCartCount() {
        CartItemsList cartItemsList = cartService.findAllAndCount();
        return globalCommonService.getResponseOfObject(Optional.of(cartItemsList), HttpStatus.OK);
    }

    @GetMapping("/cartCount")
    private ResponseEntity<?> getCartItemCountById(@RequestParam(defaultValue = "") String productId, @RequestParam(defaultValue = "") String variant) {
        Count itemCount = new Count(cartService.findCountByIdAndVariant(productId, variant));
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
    private ResponseEntity<?> deleteFromCart(String id, String variant) {
        String message = "Product has been removed from cart";
        HttpStatus status = HttpStatus.OK;
        try {
            if (Objects.equals(id, "")) throw new RuntimeException("Invalid product id");
            cartService.delete(id, variant);
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
