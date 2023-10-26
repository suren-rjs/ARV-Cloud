package com.arv.auth.controller;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.CouponCode;
import com.arv.auth.model.dto.request.CouponCodeDTO;
import com.arv.auth.model.dto.response.CouponCodeList;
import com.arv.auth.service.CouponCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/couponCode", produces = MediaType.APPLICATION_JSON_VALUE)
public class CouponCodeController {
    @Value("${product.pageSize}")
    protected int PAGE_COUNT;

    @Autowired
    protected CouponCodeService couponCodeService;
    @Autowired
    protected GlobalCommonService globalCommonService;

    @GetMapping("/all")
    private ResponseEntity<?> getAll(
            @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false) String search) {
        Pageable pageable = PageRequest.of(page, PAGE_COUNT);

        Page<CouponCode> couponCodes;

        if (search != null && !search.isEmpty()) {
            couponCodes = couponCodeService.findByCodeContainingIgnoreCase(search, pageable);
        } else {
            couponCodes = couponCodeService.findAll(pageable);
        }

        CouponCodeList couponCodeList = new CouponCodeList(
                couponCodes.getContent(),
                page,
                globalCommonService.getTotalPage(couponCodes.getTotalPages()),
                couponCodes.getNumberOfElements()
        );
        return globalCommonService.getResponseOfObject(Optional.of(couponCodeList), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> findById(@PathVariable String id) {
        return globalCommonService.getResponseOfObject(Optional.of(couponCodeService.findOneById(id)), HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<?> addCoupon(@RequestBody CouponCodeDTO data) {
        return globalCommonService.getResponseOfObject(Optional.of(couponCodeService.save(data)), HttpStatus.OK);
    }

    @PutMapping
    private ResponseEntity<?> updateCoupon(@RequestBody CouponCodeDTO data) {
        return globalCommonService.getResponseOfObject(Optional.of(couponCodeService.update(data)), HttpStatus.OK);
    }

    @DeleteMapping
    private ResponseEntity<?> deleteCoupon(@RequestParam String id) {
        HttpStatus status = HttpStatus.OK;
        String message = "Coupon Code deletion success !";
        try {
            couponCodeService.delete(id);
        } catch (Exception e) {
            status = HttpStatus.NOT_ACCEPTABLE;
            message = e.getMessage();
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }
}
