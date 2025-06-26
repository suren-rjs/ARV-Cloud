package com.arv.auth.service;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.CouponCode;
import com.arv.auth.model.dto.request.CouponCodeDTO;
import com.arv.auth.repository.crud.CouponCodeRepository;
import com.arv.auth.repository.data.CouponCodeDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;

@SuppressWarnings("unused")
@Service
public class CouponCodeService implements CouponCodeDtoRepository {

    @Autowired
    protected CouponCodeRepository couponCodeRepository;

    @Autowired
    protected GlobalCommonService globalCommonService;

    @Override
    public CouponCode save(CouponCodeDTO couponCodeDTO) {
        CouponCode couponCode = (CouponCode) globalCommonService.convertObject(couponCodeDTO, CouponCode.class);
        return couponCodeRepository.save(couponCode);
    }

    @Override
    public Page<CouponCode> findAll(Pageable pageable) {
        return couponCodeRepository.findAll(pageable);
    }


    @Override
    public CouponCode findOneById(String id) {
        couponCodeRepository.existsById(id);
        return couponCodeRepository.findById(id).orElseThrow(() -> new NotFoundException("Invalid id"));
    }

    @Override
    public Optional<CouponCode> findOneByCouponCode(String couponCode) {
        return couponCodeRepository.findOneByCouponCode(couponCode);
    }

    @Override
    public CouponCode update(CouponCodeDTO couponCodeDTO) {
        couponCodeRepository.findById(couponCodeDTO.getId()).orElseThrow(() -> new NotFoundException("Invalid id"));
        CouponCode couponCode = (CouponCode) globalCommonService.convertObject(couponCodeDTO, CouponCode.class);
        return couponCodeRepository.save(couponCode);
    }

    @Override
    public void delete(String id) {
        couponCodeRepository.findById(id).orElseThrow(() -> new NotFoundException("Invalid id"));
        couponCodeRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        couponCodeRepository.deleteAll();
    }

    @Override
    public Page<CouponCode> findByCodeContainingIgnoreCase(String search, Pageable pageable) {
        return couponCodeRepository.findByCouponCodeContainingIgnoreCase(search, pageable);
    }
}
