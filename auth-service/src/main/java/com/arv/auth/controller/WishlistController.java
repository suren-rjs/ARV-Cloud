package com.arv.auth.controller;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.dto.request.wishlist.WishlistDto;
import com.arv.auth.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "/wishlist", produces = MediaType.APPLICATION_JSON_VALUE)
public class WishlistController {
    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private GlobalCommonService globalCommonService;

    @GetMapping
    private ResponseEntity<?> getAllFromWishlist() {
        return globalCommonService.getResponseOfObject(Optional.of(wishlistService.findAll()), HttpStatus.OK);
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
