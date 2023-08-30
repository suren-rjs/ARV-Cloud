package com.arv.auth.repository.data;

import com.arv.auth.model.document.HomeBanner;
import com.arv.auth.model.dto.request.HomeBannerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HomeBannerDtoRepository {
    Page<HomeBanner> findAll(Pageable pageable);
    Page<HomeBanner> searchByName(String name, Pageable pageable);

    HomeBanner findById(String id);

    HomeBanner save(HomeBannerDTO homeBannerDTO);

    void delete(String id);

    HomeBanner update(HomeBannerDTO homeBannerDTO);
}
