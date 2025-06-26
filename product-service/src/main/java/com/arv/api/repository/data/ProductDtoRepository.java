package com.arv.api.repository.data;

import com.arv.api.model.document.ProductItem;
import com.arv.api.model.dto.request.PriceRequest;
import com.arv.api.model.dto.request.ProductItemDTO;
import com.arv.api.model.dto.response.ProductList;

import java.util.List;

@SuppressWarnings("unused")
public interface ProductDtoRepository {
    <S extends ProductItem> void save(ProductItemDTO productItemDto);

    void deleteById(String id) throws Exception;

    <S extends ProductItem> void updateById(ProductItemDTO productItem) throws Exception;

    <S extends ProductItem> ProductItem getById(String id) throws Exception;

    <S extends ProductItem> List<ProductItem> getAllProducts();

    void saveAll(List<ProductItemDTO> productItemDTOList);

    ProductList search(String majorCategoryId, String categoryId,  String subCategoryId,  String storeId, String keyword, double priceFrom, double priceTo, int page);

    ProductList findProductByIdsIn(String[] ids, int page);

    void preserveOrder(String id, int qty, String variant);

    void unPreserve(String id, int qty, String variant);
    Double getProductPriceByVariant(PriceRequest priceRequest);
    void changeStatus(String productId, Boolean bool);
}
