package com.arv.userservice.repository.crud;


import com.arv.userservice.model.document.WishlistProducts;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends MongoRepository<WishlistProducts, String> {

    <S extends WishlistProducts> List<S> findAllByCustomerId(String id);

    <S extends WishlistProducts> Optional<S> findOneByProductId(String id);

    <S extends WishlistProducts> Optional<S> findOneByProductIdAndCustomerId(String productId, String customerId);

}
