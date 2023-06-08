package com.api.arv.repository.crud;

import com.api.arv.model.document.ProductCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductCategoryRepository extends MongoRepository<ProductCategory, String> {
    <S extends ProductCategory> Optional<S> findOneById(String id);

    Optional<ProductCategory> findOneByName(String name);
}
