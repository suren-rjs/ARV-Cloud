package com.api.arv.repository.crud;

import com.api.arv.model.document.ProductSubCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductSubCategoryRepository extends MongoRepository<ProductSubCategory, String> {
    Optional<ProductSubCategory> findOneById(String id);

    <S extends ProductSubCategory> List<S> findAllByCategoryId(String categoryId);

    Optional<ProductSubCategory> findOneByCategoryIdAndName(String categoryId, String name);
}
