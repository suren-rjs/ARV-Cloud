package com.arv.api.repository.data;

import com.arv.api.model.document.ProductItem;
import com.arv.api.model.dto.request.product.ProductItemDTO;
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

    ProductList search(String categoryId, String keyword, double priceFrom, double priceTo, int page);

    ProductList findProductByIdsIn(String[] ids, int page);
}
