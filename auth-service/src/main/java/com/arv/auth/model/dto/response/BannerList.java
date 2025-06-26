package com.arv.auth.model.dto.response;

import com.arv.auth.model.document.HomeBanner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerList {
    private List<HomeBanner> list;
    private int currentPage;
    private int totalCount;
    private int totalPages;
}
