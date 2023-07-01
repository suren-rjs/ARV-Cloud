package com.arv.auth.controller;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.Address;
import com.arv.auth.model.dto.request.address.AddressDto;
import com.arv.auth.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "/address", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddressController {

    @Autowired
    private GlobalCommonService globalCommonService;
    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<?> addNewAddress(@RequestBody AddressDto addressDto) {
        String message = "New address added";
        HttpStatus status = HttpStatus.OK;
        try {
            addressService.save(addressDto);
        } catch (Exception e) {
            message = e.getMessage();
            status = HttpStatus.NOT_ACCEPTABLE;
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @PutMapping
    public ResponseEntity<?> updateAddress(@RequestBody Address address) {
        String message = "address updated";
        HttpStatus status = HttpStatus.OK;
        try {
            addressService.update(address);
        } catch (Exception e) {
            message = e.getMessage();
            status = HttpStatus.NOT_ACCEPTABLE;
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @GetMapping
    public ResponseEntity<?> getAddress() {
        return globalCommonService.getResponseOfObject(Optional.of(addressService.findAll()), HttpStatus.OK);
    }

    @DeleteMapping
    private ResponseEntity<?> deleteAddress(String id) {
        String message = "Address has been deleted";
        HttpStatus status = HttpStatus.OK;
        try {
            if (Objects.equals(id, "")) throw new RuntimeException("Invalid id");
            addressService.delete(id);
        } catch (Exception e) {
            message = e.getMessage();
            status = HttpStatus.NOT_ACCEPTABLE;
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @DeleteMapping("/deleteAll")
    private ResponseEntity<?> deleteAll() {
        String message = "All addresses are deleted";
        HttpStatus status = HttpStatus.OK;
        addressService.deleteAll();
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }
}
