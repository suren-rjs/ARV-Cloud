package com.arv.auth.controller;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.dto.request.HomeBannerDTO;
import com.arv.auth.service.HomeBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/homeBanner", produces = MediaType.APPLICATION_JSON_VALUE)
public class HomeBannerController {
    @Value("${product.pageSize}")
    private int PAGE_COUNT;

    @Autowired
    protected HomeBannerService homeBannerService;
    @Autowired
    protected GlobalCommonService globalCommonService;

    @GetMapping
    private ResponseEntity<?> getAll(@RequestParam(required = false, defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_COUNT);
        return globalCommonService.getResponseOfObject(Optional.of(homeBannerService.findAll(pageable)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> findById(@PathVariable String id) {
        return globalCommonService.getResponseOfObject(Optional.of(homeBannerService.findById(id)), HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<?> addBanner(@RequestBody HomeBannerDTO homeBannerDTO) {
        return globalCommonService.getResponseOfObject(Optional.of(homeBannerService.save(homeBannerDTO)), HttpStatus.OK);
    }

    @PutMapping
    private ResponseEntity<?> updateBanner(@RequestBody HomeBannerDTO homeBannerDTO) {
        return globalCommonService.getResponseOfObject(Optional.of(homeBannerService.update(homeBannerDTO)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteBanner(@PathVariable String id) {
        HttpStatus status = HttpStatus.OK;
        String message = "Home banner deletion success !";
        try {
            homeBannerService.delete(id);
        } catch (Exception e) {
            status = HttpStatus.NOT_ACCEPTABLE;
            message = e.getMessage();
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }
}
