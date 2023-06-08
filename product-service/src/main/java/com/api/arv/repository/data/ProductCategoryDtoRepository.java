package com.api.arv.repository.data;

import com.api.arv.model.document.ProductCategory;
import com.api.arv.model.document.ProductSubCategory;
import com.api.arv.model.document.ProductSubSubCategory;

import java.util.Optional;

@SuppressWarnings("unused")
public interface ProductCategoryDtoRepository {
    <S extends ProductCategory> ProductCategory saveCategory(ProductCategory productCategory);

    void deleteCategoryById(String id);

    <S extends ProductCategory> Optional<ProductCategory> getOneCategoryById(String id);

    <S extends ProductCategory> ProductCategory updateCategory(ProductCategory productCategory);

    <S extends ProductSubCategory> ProductSubCategory saveSubCategory(ProductSubCategory productSubCategory);

    void deleteSubCategoryById(String id);

    <S extends ProductSubCategory> Optional<ProductSubCategory> getOneSubCategoryById(String id);

    <S extends ProductSubCategory> ProductSubCategory updateSubCategory(ProductSubCategory productSubCategory);

    <S extends ProductSubSubCategory> ProductSubSubCategory saveSubSubCategory(ProductSubSubCategory productSubSubCategory);

    void deleteSubSubCategoryById(String id);

    <S extends ProductSubSubCategory> Optional<ProductSubSubCategory> getOneSubSubCategoryById(String id);

    <S extends ProductSubSubCategory> ProductSubSubCategory updateSubSubCategory(ProductSubSubCategory productSubSubCategory);

    <S extends ProductCategory> Optional<ProductCategory> getOneCategoryByName(String name);

    <S extends ProductSubCategory> Optional<ProductSubCategory> getOneSubCategoryByCategoryIdAndName(String categoryId, String name);

    <S extends ProductSubSubCategory> Optional<ProductSubSubCategory> getOneSubSubCategoryByCategoryIdAndSubCategoryIdAndName(String categoryId, String subCategoryId, String name);
}
