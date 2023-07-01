package com.arv.auth.repository.crud;

import com.arv.auth.model.document.Wishlist;
import com.arv.auth.model.dto.request.cart.CartDto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WishlistRepository extends MongoRepository<Wishlist, String> {
    void deleteByIdAndUserId(String id, String userId);

    void deleteAllByUserId(String id);

    List<Wishlist> findAllByUserId(String id);
}
