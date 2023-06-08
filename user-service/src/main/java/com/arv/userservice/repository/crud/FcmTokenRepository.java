package com.arv.userservice.repository.crud;


import com.arv.userservice.model.document.FcmToken;
import org.springframework.data.mongodb.repository.MongoRepository;

@SuppressWarnings("unused")
public interface FcmTokenRepository extends MongoRepository<FcmToken, String> {
    FcmToken findByUserId(String id);
}
