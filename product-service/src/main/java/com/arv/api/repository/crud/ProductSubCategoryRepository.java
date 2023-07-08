package com.arv.api.repository.crud;

import com.arv.api.model.document.ProductSubCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductSubCategoryRepository extends MongoRepository<ProductSubCategory, String> {
    <S extends ProductSubCategory> Optional<S> findOneById(String id);

    <S extends ProductSubCategory> List<S> findAllByCategoryId(String categoryId);

    Optional<ProductSubCategory> findOneSubSubCategoryByCategoryIdAndName(String categoryId, String name);
    Optional<ProductSubCategory> findOneSubSubCategoryByCategoryIdAndId(String categoryId, String id);

    Optional<ProductSubCategory> findOneByCategoryIdAndNameContainingIgnoreCase(String categoryId, String name);
}
