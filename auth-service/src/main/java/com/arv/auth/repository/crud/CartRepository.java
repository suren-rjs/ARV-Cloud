package com.arv.auth.repository.crud;

import com.arv.auth.model.document.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CartRepository extends MongoRepository<Cart, String> {
    void deleteByIdAndUserId(String id, String userId);

    Cart findByIdAndUserId(String id, String userId);

    void deleteAllByUserId(String id);

    List<Cart> findAllByUserId(String id);

    boolean existsByProductIdAndUserIdAndVariant(String id, String userId, String variant);

    Cart findByProductIdAndUserIdAndVariant(String productId, String id, String variant);
}
