package com.api.arv.controller;

import com.api.arv.helper.GlobalCommonService;
import com.api.arv.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@SuppressWarnings("All")
@RestController
@RequestMapping(value = "/public", produces = MediaType.APPLICATION_JSON_VALUE)
public class PublicController {

    @Autowired
    private GlobalCommonService globalCommonService;

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts(@RequestParam("keyword") String keyword, @RequestParam("priceFrom") double priceFrom, @RequestParam("priceTo") double priceTo) {
        if (keyword.replaceAll(" ", "").isEmpty()) keyword = "";
        return globalCommonService.getResponseOfObject(Optional.of(productService.search(keyword, priceFrom, priceTo)), HttpStatus.OK);
    }
}
