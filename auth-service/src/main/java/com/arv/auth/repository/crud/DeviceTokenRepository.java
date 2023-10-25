package com.arv.auth.repository.crud;

import com.arv.auth.model.document.DeviceToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DeviceTokenRepository extends MongoRepository<DeviceToken, String> {
    Optional<DeviceToken> findOneByUsername(String username);
}
