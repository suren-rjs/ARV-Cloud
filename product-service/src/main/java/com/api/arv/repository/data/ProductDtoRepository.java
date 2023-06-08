package com.api.arv.repository.data;

import com.api.arv.model.document.ProductItem;
import com.api.arv.model.dto.request.product.ProductItemDTO;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface ProductDtoRepository {
    <S extends ProductItem> void save(ProductItemDTO productItemDto);

    void deleteById(String id) throws NotFoundException;

    <S extends ProductItem> void updateById(ProductItemDTO productItem) throws Exception;

    <S extends ProductItem> Optional<S> getById(String id) throws NotFoundException;

    <S extends ProductItem> List<ProductItem> getAllProducts();

    void saveAll(List<ProductItemDTO> productItemDTOList);
}
