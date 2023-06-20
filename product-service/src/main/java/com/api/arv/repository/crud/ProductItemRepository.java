package com.api.arv.repository.crud;

import com.api.arv.model.document.ProductItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ProductItemRepository extends MongoRepository<ProductItem, String> {
    <S extends ProductItem> Optional<S> findOneById(String id);

    List<ProductItem> findByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

    List<ProductItem> findBySellingPriceBetween(double minSellingPrice, double maxSellingPrice);

    List<ProductItem> findByMrpPriceBetween(double minMrpPrice, double maxMrpPrice);

    List<ProductItem> findBySellingPriceGreaterThanEqual(double minSellingPrice);

    List<ProductItem> findByMrpPriceGreaterThanEqual(double minSellingPrice);

    List<ProductItem> findBySellingPriceLessThanEqual(double maxSellingPrice);

    List<ProductItem> findByMrpPriceLessThanEqual(double maxMrpPrice);
}
