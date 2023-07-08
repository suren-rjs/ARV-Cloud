package com.arv.api.repository.crud;

import com.arv.api.model.document.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepository extends MongoRepository<ProductCategory, String> {
    <S extends ProductCategory> Optional<S> findOneById(String id);

    Optional<ProductCategory> findOneByName(String name);

    Optional<ProductCategory> findOneByNameOrId(String name, String id);

    Page<ProductCategory> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
