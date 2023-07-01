package com.arv.auth.controller;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.Wishlist;
import com.arv.auth.model.dto.request.wishlist.WishlistDto;
import com.arv.auth.model.rest.ProductList;
import com.arv.auth.model.rest.Products;
import com.arv.auth.service.WishlistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "/wishlist", produces = MediaType.APPLICATION_JSON_VALUE)
public class WishlistController {

    @Value("${gateway.url}")
    private String gatewayUrl;
    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private GlobalCommonService globalCommonService;

    @GetMapping
    private ResponseEntity<?> getAllFromWishlist(@RequestParam(required = false, defaultValue = "0") int pageNumber) throws JsonProcessingException {
        String[] productIds = wishlistService.findAll().stream().map(Wishlist::getProductId).toArray(String[]::new);

        String uriWithParams = gatewayUrl+"/public/list?page=" + pageNumber;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Set the request body
        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(new Products(productIds));
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        return globalCommonService.getResponseOfObject(Optional.ofNullable(globalCommonService.postForObject(uriWithParams, entity, ProductList.class)), HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<?> addToWishlist(@RequestBody WishlistDto wishlistDto) {
        String message = "Product added to wishlist";
        HttpStatus status = HttpStatus.OK;
        try {
            if (Objects.equals(wishlistDto.getProductId(), "")) throw new RuntimeException("Invalid product id");
            wishlistService.save(wishlistDto);
        } catch (Exception e) {
            message = e.getMessage();
            status = HttpStatus.NOT_ACCEPTABLE;
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @DeleteMapping
    private ResponseEntity<?> deleteFromWishlist(String id) {
        String message = "Product has been removed from wishlist";
        HttpStatus status = HttpStatus.OK;
        try {
            if (Objects.equals(id, "")) throw new RuntimeException("Invalid product id");
            wishlistService.delete(id);
        } catch (Exception e) {
            message = e.getMessage();
            status = HttpStatus.NOT_ACCEPTABLE;
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @DeleteMapping("/deleteAll")
    private ResponseEntity<?> deleteAllFromWishlist() {
        String message = "All products are removed from wishlist!";
        HttpStatus status = HttpStatus.OK;
        wishlistService.deleteAll();
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }
}
