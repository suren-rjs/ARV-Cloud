package com.arv.api.controller;

import com.arv.api.helper.GlobalCommonService;
import com.arv.api.model.document.ProductItem;
import com.arv.api.model.dto.request.CartDto;
import com.arv.api.model.dto.request.Orders;
import com.arv.api.model.dto.request.Products;
import com.arv.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                                            @RequestParam(required = false, defaultValue = "") String majorCategoryId,
                                            @RequestParam(required = false, defaultValue = "") String subCategoryId,
                                            @RequestParam(required = false, defaultValue = "") String keyword,
                                            @RequestParam(required = false, defaultValue = "0.0") Double priceFrom,
                                            @RequestParam(required = false, defaultValue = "0.0") Double priceTo,
                                            @RequestParam(required = false, defaultValue = "0") int page) {
        return globalCommonService.getResponseOfObject(Optional.of(productService.search(majorCategoryId, categoryId, subCategoryId, keyword, priceFrom, priceTo, page)), HttpStatus.OK);
    }

    @PostMapping("/list")
    public ResponseEntity<?> getAllProductsByIdsWithPage(@RequestBody Products products, @RequestParam(required = false, defaultValue = "0") int page) {
        return globalCommonService.getResponseOfObject(Optional.of(productService.findProductByIdsIn(products.getIds(), page)), HttpStatus.OK);
    }

    @PostMapping("/listAll")
    public ResponseEntity<?> getAllProductsByIds(@RequestBody Products products) {
        List<ProductItem> productsList = productService.findProductByIdsIn(products.getIds());
        return globalCommonService.getResponseOfObject(Optional.of(productsList), HttpStatus.OK);
    }

    @GetMapping("/products/image/{productId}")
    public ResponseEntity<?> getProductImages(@PathVariable("productId") String productId) {
        return productService.downloadFile(productId);
    }

    @PostMapping("/getStatment")
    public ResponseEntity<?> getProductImages(@RequestBody Orders orders) {
        List<ProductItem> productsList = productService.findProductByIdsIn(orders.getList().stream().map(CartDto::getProductId).toArray(String[]::new));
        Double totalAmount = 0.0;
        Map<String, Integer> productQuantityMap = new HashMap<>();

        orders.getList().forEach(cartDto -> {
            String productId = cartDto.getProductId();
            int quantity = cartDto.getQty();
            productQuantityMap.put(productId, productQuantityMap.getOrDefault(productId, 0) + quantity);
        });

        int i = 0;
        for (ProductItem productItem : productsList) {
            String productId = productItem.getId();
            int variantIndex = globalCommonService.findIndex(productItem.getProductVariation(), orders.getList().get(i++).getVariant());
            Double sellingPriceByVarient = productItem.getSellingPrice()[variantIndex];
            int quantity = productQuantityMap.getOrDefault(productId, 0);
            totalAmount += quantity * sellingPriceByVarient;
        }

        return new ResponseEntity<>(totalAmount, HttpStatus.OK);
    }
}
