package com.arv.api.repository.crud;

import com.arv.api.model.document.ProductItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ProductItemRepository extends MongoRepository<ProductItem, String> {
    Optional<ProductItem> findOneById(String id);

    Page<ProductItem> findByCategoryIdContainingIgnoreCaseAndProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndSellingPriceBetweenOrMrpPriceBetween(String categoryId, String productName, String description, double sStartPrice, double sEndPrice, double mStartPrice, double mEndPrice, Pageable pageable);

    Page<ProductItem> findAllByIdIn(String[] ids, Pageable pageable);
}
