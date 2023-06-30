package com.arv.api.repository.crud;

import com.arv.api.model.document.ProductSubSubCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductSubSubCategoryRepository extends MongoRepository<ProductSubSubCategory, String> {
    <S extends ProductSubSubCategory> Optional<S> findOneById(String id);

    <S extends ProductSubSubCategory> List<S> findAllByCategoryIdAndSubCategoryId(String categoryId, String subCategoryId);

    Optional<ProductSubSubCategory> findOneByCategoryIdAndSubCategoryIdAndName(String categoryId, String subCategoryId, String name);

    Optional<ProductSubSubCategory> findOneByCategoryIdAndSubCategoryIdAndNameContainingIgnoreCase(String categoryId, String subCategoryId, String name);

}
