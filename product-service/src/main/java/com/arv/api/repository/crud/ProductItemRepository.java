package com.arv.api.repository.crud;

import com.arv.api.model.document.ProductItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ProductItemRepository extends MongoRepository<ProductItem, String> {
    <S extends ProductItem> Optional<S> findOneById(String id);

    Page<ProductItem> findByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);

    Page<ProductItem> findBySellingPriceBetween(double minSellingPrice, double maxSellingPrice, Pageable pageable);

    Page<ProductItem> findByMrpPriceBetween(double minMrpPrice, double maxMrpPrice, Pageable pageable);

    Page<ProductItem> findBySellingPriceGreaterThanEqual(double minSellingPrice, Pageable pageable);

    Page<ProductItem> findByMrpPriceGreaterThanEqual(double minSellingPrice, Pageable pageable);

    Page<ProductItem> findBySellingPriceLessThanEqual(double maxSellingPrice, Pageable pageable);

    Page<ProductItem> findByMrpPriceLessThanEqual(double maxMrpPrice, Pageable pageable);
    Page<ProductItem> findAllByIdIn(String[] ids, Pageable pageable);
}
