package com.arv.auth.repository.data;

import com.arv.auth.model.document.CouponCode;
import com.arv.auth.model.dto.request.CouponCodeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CouponCodeDtoRepository {
    CouponCode save(CouponCodeDTO couponCodeDTO);

    Page<CouponCode> findAll(Pageable pageable);

    Optional<CouponCode> findOneByCouponCode(String couponCode);

    CouponCode findOneById(String id);

    CouponCode update(CouponCodeDTO couponCodeDTO);

    void delete(String id);

    void deleteAll();

    Page<CouponCode> findByCodeContainingIgnoreCase(String search, Pageable pageable);
}
