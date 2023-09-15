package com.arv.auth.controller;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.CouponCode;
import com.arv.auth.model.document.HomeBanner;
import com.arv.auth.model.document.Order;
import com.arv.auth.model.dto.response.BannerList;
import com.arv.auth.model.enums.HomeBannerSection;
import com.arv.auth.repository.crud.OrderRepository;
import com.arv.auth.service.CouponCodeService;
import com.arv.auth.service.HomeBannerService;
import com.arv.auth.service.OrderService;
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
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiController {
    @Value("${product.pageSize}")
    protected int PAGE_COUNT;

    @Autowired
    protected HomeBannerService homeBannerService;
    @Autowired
    protected GlobalCommonService globalCommonService;
    @Autowired
    protected CouponCodeService couponCodeService;

    @Autowired
    protected OrderService orderService;
    @Autowired
    protected OrderRepository orderRepository;

    @GetMapping("/homeBanners")
    protected ResponseEntity<?> getHomeBanners(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "") String searchName, @RequestParam(required = false, defaultValue = "SECTION_1") HomeBannerSection homeBannerSection) {
        Pageable pageable = PageRequest.of(page, PAGE_COUNT);
        Page<HomeBanner> banners = homeBannerService.searchByNameAndBanner(homeBannerSection, searchName, pageable);
        return globalCommonService.getResponseOfObject(Optional.of(new BannerList(banners.getContent(), page, banners.getNumberOfElements(), globalCommonService.getTotalPage(banners.getTotalPages()))), HttpStatus.OK);
    }

    @PostMapping("/applyCouponCode")
    protected ResponseEntity<?> applyCouponCode(@RequestParam(value = "code") String code, @RequestParam("orderId") String orderId) {
        Optional<CouponCode> couponCode = couponCodeService.findOneByCouponCode(code);
        if (!couponCode.isPresent()) {
            return globalCommonService.getResponseEntityByMessageAndStatus("Invalid coupon", HttpStatus.NOT_ACCEPTABLE);
        }

        CouponCode availableCode = couponCode.get();
        Double minOrderValue = availableCode.getMinOrderPrice();
        Double maxDiscountPrice = availableCode.getMaxDiscountPrice();
        double discountPercentage = availableCode.getDiscountPercentage();

        Order order = orderService.findOneById(orderId);
        Double orderPrice = order.getTotalAmount();

        if (orderPrice < minOrderValue) {
            return globalCommonService.getResponseEntityByMessageAndStatus("Minimum order value not met for this coupon", HttpStatus.BAD_REQUEST);
        }

        double calculatedDiscount = (discountPercentage / 100) * orderPrice;
        if (calculatedDiscount > maxDiscountPrice) {
            calculatedDiscount = maxDiscountPrice;
        }

        double discountedAmount = orderPrice - calculatedDiscount;

        order.setCouponCode(code);
        order.setCouponDiscountPercentage(discountPercentage);
        order.setDiscountedPrice(calculatedDiscount);
        order.setFinalPrice(discountedAmount);
        orderRepository.save(order);

        return globalCommonService.getResponseEntityByMessageAndStatus("Coupon applied successfully! Discounted amount: " + discountedAmount, HttpStatus.OK);
    }

    @PostMapping("/clearCoupon")
    protected ResponseEntity<?> clearCoupon(@RequestParam("orderId") String orderId) {
        Order order = orderService.findOneById(orderId);
        Double orderPrice = order.getTotalAmount();
        order.setCouponCode("");
        order.setCouponDiscountPercentage(0.0);
        order.setDiscountedPrice(0.0);
        order.setFinalPrice(orderPrice);
        orderRepository.save(order);

        return globalCommonService.getResponseEntityByMessageAndStatus("Coupon has been removed from your order!", HttpStatus.OK);
    }
}
