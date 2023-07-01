package com.arv.auth.repository.crud;

import com.arv.auth.model.document.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CartRepository extends MongoRepository<Cart, String> {
    void deleteByIdAndUserId(String id, String userId);

    void deleteAllByUserId(String id);

    List<Cart> findAllByUserId(String id);
}
