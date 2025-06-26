package com.arv.auth.repository.crud;

import com.arv.auth.model.document.HomeBanner;
import com.arv.auth.model.enums.HomeBannerSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HomeBannerRepository extends MongoRepository<HomeBanner, String> {
    Page<HomeBanner> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<HomeBanner> findByHomeBannerSectionAndNameContainingIgnoreCase(
            HomeBannerSection homeBannerSection, String name, Pageable pageable);
}
