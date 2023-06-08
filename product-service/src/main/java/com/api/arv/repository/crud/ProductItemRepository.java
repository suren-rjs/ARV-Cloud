package com.api.arv.repository.crud;

import com.api.arv.model.document.ProductItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ProductItemRepository extends MongoRepository<ProductItem, String> {
    <S extends ProductItem> Optional<S> findOneById(String id);
}
