package com.arv.api.repository.data;

import com.arv.api.model.document.ProductCategory;
import com.arv.api.model.document.ProductSubCategory;
import com.arv.api.model.dto.request.ProductCategoryDTO;
import com.arv.api.model.dto.request.ProductSubCategoryDTO;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ProductCategoryDtoRepository {
    <S extends ProductCategory> ProductCategory saveCategory(ProductCategory productCategory);

    void deleteCategoryById(String id);

    <S extends ProductCategory> Optional<ProductCategory> getOneCategoryById(String id);

    <S extends ProductCategory> ProductCategory updateCategory(ProductCategoryDTO productCategory);

    <S extends ProductSubCategory> ProductSubCategory saveSubCategories(ProductSubCategoryDTO productSubCategory);

    void deleteSubCategoriesById(String id);

    <S extends ProductSubCategory> Optional<ProductSubCategory> getOneSubCategoriesById(String id);

    ProductSubCategory updateSubCategories(ProductSubCategoryDTO productSubCategory);

    <S extends ProductCategory> Optional<ProductCategory> getOneCategoryByName(String name);

    <S extends ProductCategory> Optional<ProductCategory> findOneCategoryByNameOrId(String name);


    <S extends ProductSubCategory> Optional<ProductSubCategory> getOneByCategoryIdAndName(String categoryId, String name);

    <S extends ProductSubCategory> Optional<ProductSubCategory> getOneByCategoryIdAndId(String categoryId, String name);
}
