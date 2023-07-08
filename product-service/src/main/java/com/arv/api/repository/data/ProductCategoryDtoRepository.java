package com.arv.api.repository.data;

import com.arv.api.model.document.ProductCategory;
import com.arv.api.model.document.ProductSubCategory;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ProductCategoryDtoRepository {
    <S extends ProductCategory> ProductCategory saveCategory(ProductCategory productCategory);

    void deleteCategoryById(String id);

    <S extends ProductCategory> Optional<ProductCategory> getOneCategoryById(String id);

    <S extends ProductCategory> ProductCategory updateCategory(ProductCategory productCategory);

    <S extends ProductSubCategory> ProductSubCategory saveSubCategories(ProductSubCategory productSubCategory);

    void deleteSubCategoriesById(String id);

    <S extends ProductSubCategory> Optional<ProductSubCategory> getOneSubCategoriesById(String id);

    <S extends ProductSubCategory> ProductSubCategory updateSubCategories(ProductSubCategory productSubCategory);

    <S extends ProductCategory> Optional<ProductCategory> getOneCategoryByName(String name);
    <S extends ProductCategory> Optional<ProductCategory> findOneCategoryByNameOrId(String name);


    <S extends ProductSubCategory> Optional<ProductSubCategory> getOneSubCategoriesByCategoryIdAndName(String categoryId, String name);
    <S extends ProductSubCategory> Optional<ProductSubCategory> getOneSubCategoriesByCategoryIdAndId(String categoryId, String name);
}
