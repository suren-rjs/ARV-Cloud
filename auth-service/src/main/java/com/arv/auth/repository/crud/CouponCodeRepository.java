package com.arv.auth.repository.crud;

import com.arv.auth.model.document.CouponCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CouponCodeRepository extends MongoRepository<com.arv.auth.model.document.CouponCode, String> {
    Page<CouponCode> findByCouponCodeContainingIgnoreCase(String search, Pageable pageable);

    Optional<CouponCode> findOneByCouponCode(String code);
}
