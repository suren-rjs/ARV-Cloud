package com.arv.api.repository.crud;

import com.arv.api.model.document.ProductItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ProductItemRepository extends MongoRepository<ProductItem, String> {
    Optional<ProductItem> findOneById(String id);

    Page<ProductItem> findByCategoryIdContainingIgnoreCaseOrProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String categoryId, String name, String description, Pageable pageable);

    Page<ProductItem> findByCategoryIdContainingIgnoreCaseAndSellingPriceBetween(String categoryId, double minSellingPrice, double maxSellingPrice, Pageable pageable);

    Page<ProductItem> findByCategoryIdContainingIgnoreCaseAndMrpPriceBetween(String categoryId, double minMrpPrice, double maxMrpPrice, Pageable pageable);

    Page<ProductItem> findByCategoryIdContainingIgnoreCaseAndSellingPriceGreaterThanEqual(String categoryId, double minSellingPrice, Pageable pageable);

    Page<ProductItem> findByCategoryIdContainingIgnoreCaseAndMrpPriceGreaterThanEqual(String categoryId, double minSellingPrice, Pageable pageable);

    Page<ProductItem> findByCategoryIdContainingIgnoreCaseAndSellingPriceLessThanEqual(String categoryId, double maxSellingPrice, Pageable pageable);

    Page<ProductItem> findByCategoryIdContainingIgnoreCaseAndMrpPriceLessThanEqual(String categoryId, double maxMrpPrice, Pageable pageable);

    Page<ProductItem> findAllByIdIn(String[] ids, Pageable pageable);
}
