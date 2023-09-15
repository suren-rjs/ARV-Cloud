package com.arv.api.controller;

import com.arv.api.helper.GlobalCommonService;
import com.arv.api.model.dto.request.StoreDTO;
import com.arv.api.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/store", produces = MediaType.APPLICATION_JSON_VALUE)
public class StoreInfoController {
    @Autowired
    private StoreService storeService;

    @Autowired
    private GlobalCommonService globalCommonService;

    @PostMapping
    public ResponseEntity<?> addStoreInfo(@RequestBody StoreDTO storeInfo) {
        storeService.save(storeInfo);
        return globalCommonService.getResponseEntityByMessageAndStatus("New store info added", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateStoreInfo(@RequestBody StoreDTO storeInfo) {
        String message = "Store info updated";
        HttpStatus response = HttpStatus.OK;
        try {
            storeService.updateById(storeInfo);
        } catch (Exception e) {
            message = "unable to update details";
            response = HttpStatus.NOT_ACCEPTABLE;
        }

        return globalCommonService.getResponseEntityByMessageAndStatus(message, response);
    }

    @GetMapping("/deliveryPrice")
    public ResponseEntity<?> getDeliveryPrice(@RequestParam(defaultValue = "") String locationId) {
        // TODO: Delivery Price Calculation implementation
        throw new RuntimeException("Unimplemented method");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStoreInfo(@PathVariable String id) {
        String message = "Store info deleted";
        HttpStatus response = HttpStatus.OK;
        try {
            storeService.deleteById(id);
        } catch (Exception e) {
            message = "invalid id";
            response = HttpStatus.NOT_ACCEPTABLE;
        }

        return globalCommonService.getResponseEntityByMessageAndStatus(message, response);
    }

    @GetMapping
    public ResponseEntity<?> findAllStores(@RequestParam(defaultValue = "0", required = false) int page, @RequestParam(defaultValue = "", required = false) String keyword) {
        return globalCommonService.getResponseOfObject(Optional.of(storeService.search(keyword, page)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findAllStores(@PathVariable String id) {
        return globalCommonService.getResponseOfObject(Optional.of(storeService.findById(id)), HttpStatus.OK);
    }
}
