package com.arv.auth.model.dto.response;

import com.arv.auth.model.document.CouponCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponCodeList {
    private List<CouponCode> list;
    private int currentPage;
    private int totalCount;
    private int totalPages;
}
