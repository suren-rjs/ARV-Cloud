package com.arv.api.repository.crud;

import com.arv.api.model.document.ProductSubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductSubCategoryRepository extends MongoRepository<ProductSubCategory, String> {
    <S extends ProductSubCategory> Optional<S> findOneById(String id);

    Page<ProductSubCategory> findAllByCategoryId(String categoryId, Pageable pageable);

    Optional<ProductSubCategory> findOneByCategoryIdAndName(String categoryId, String name);
    Optional<ProductSubCategory> findOneByCategoryIdAndId(String categoryId, String id);

    Page<ProductSubCategory> findAllByCategoryIdAndNameContainingIgnoreCase(String categoryId, String name, Pageable pageable);

    List<ProductSubCategory> findAllByCategoryId(String categoryId);

    List<ProductSubCategory> findAllByCategoryIdAndNameContainingIgnoreCase(String categoryId, String keyword);
}
