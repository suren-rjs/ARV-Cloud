package com.arv.api.service;

import com.arv.api.model.document.ProductCategory;
import com.arv.api.model.document.ProductSubCategory;
import com.arv.api.repository.crud.ProductCategoryRepository;
import com.arv.api.repository.crud.ProductSubCategoryRepository;
import com.arv.api.repository.data.ProductCategoryDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@SuppressWarnings("unused")
@Service
public class ProductCategoryService implements ProductCategoryDtoRepository {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductSubCategoryRepository productSubCategoryRepository;


    @Override
    public <S extends ProductCategory> ProductCategory saveCategory(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public void deleteCategoryById(String id) {
        productCategoryRepository.deleteById(id);
    }

    @Override
    public <S extends ProductCategory> Optional<ProductCategory> getOneCategoryById(String id) {
        return productCategoryRepository.findOneById(id);
    }

    @Override
    public <S extends ProductCategory> ProductCategory updateCategory(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public <S extends ProductSubCategory> ProductSubCategory saveSubSubCategory(ProductSubCategory productSubCategory) {
        return productSubCategoryRepository.save(productSubCategory);
    }

    @Override
    public void deleteSubSubCategoryById(String id) {
        productSubCategoryRepository.deleteById(id);
    }

    @Override
    public <S extends ProductSubCategory> Optional<ProductSubCategory> getOneSubSubCategoryById(String id) {
        return productSubCategoryRepository.findOneById(id);
    }

    @Override
    public <S extends ProductSubCategory> ProductSubCategory updateSubSubCategory(ProductSubCategory productSubCategory) {
        return productSubCategoryRepository.save(productSubCategory);
    }

    @Override
    public Optional<ProductCategory> getOneCategoryByName(String name) {
        return productCategoryRepository.findOneByName(name);
    }

    public Optional<ProductSubCategory> getOneSubSubCategoryByCategoryIdAndName(String categoryId, String name) {
        return productSubCategoryRepository.findOneSubSubCategoryByCategoryIdAndName(categoryId, name);
    }
}
