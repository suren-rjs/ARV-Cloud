package com.arv.auth.repository.crud;

import com.arv.auth.model.document.HomeBanner;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HomeBannerRepository extends MongoRepository<HomeBanner, String> {
}
