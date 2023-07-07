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

    <S extends ProductSubCategory> ProductSubCategory saveSubSubCategory(ProductSubCategory productSubCategory);

    void deleteSubSubCategoryById(String id);

    <S extends ProductSubCategory> Optional<ProductSubCategory> getOneSubSubCategoryById(String id);

    <S extends ProductSubCategory> ProductSubCategory updateSubSubCategory(ProductSubCategory productSubCategory);

    <S extends ProductCategory> Optional<ProductCategory> getOneCategoryByName(String name);


    <S extends ProductSubCategory> Optional<ProductSubCategory> getOneSubSubCategoryByCategoryIdAndName(String categoryId, String name);
}
