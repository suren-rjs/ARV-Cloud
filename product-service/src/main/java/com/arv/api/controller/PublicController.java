package com.arv.api.controller;

import com.arv.api.helper.GlobalCommonService;
import com.arv.api.model.dto.request.product.Products;
import com.arv.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getAllProducts(@RequestParam(required = false, defaultValue = "") String categoryId,
                                            @RequestParam(required = false, defaultValue = "") String keyword,
                                            @RequestParam(required = false, defaultValue = "0.0") Double priceFrom,
                                            @RequestParam(required = false, defaultValue = "0.0") Double priceTo,
                                            @RequestParam(required = false, defaultValue = "0") int page) {
        return globalCommonService.getResponseOfObject(Optional.of(productService.search(categoryId, keyword, priceFrom, priceTo, page)), HttpStatus.OK);
    }

    @PostMapping("/list")
    public ResponseEntity<?> getAllProductsByIds(@RequestBody Products products, @RequestParam(required = false, defaultValue = "0") int page) {
        return globalCommonService.getResponseOfObject(Optional.of(productService.findProductByIdsIn(products.getIds(), page)), HttpStatus.OK);
    }

    @GetMapping("/products/image/{productId}")
    public ResponseEntity<?> getProductImages(@PathVariable("productId") String productId) {
        return productService.downloadFile(productId);
    }
}
