package com.api.arv.service;

import com.api.arv.model.document.ProductCategory;
import com.api.arv.model.document.ProductSubCategory;
import com.api.arv.model.document.ProductSubSubCategory;
import com.api.arv.repository.crud.ProductCategoryRepository;
import com.api.arv.repository.crud.ProductSubCategoryRepository;
import com.api.arv.repository.crud.ProductSubSubCategoryRepository;
import com.api.arv.repository.data.ProductCategoryDtoRepository;
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
    @Autowired
    private ProductSubSubCategoryRepository productSubSubCategoryRepository;


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
    public <S extends ProductSubCategory> ProductSubCategory saveSubCategory(ProductSubCategory productSubCategory) {
        return productSubCategoryRepository.save(productSubCategory);
    }

    @Override
    public void deleteSubCategoryById(String id) {
        productSubCategoryRepository.deleteById(id);

    }

    @Override
    public <S extends ProductSubCategory> Optional<ProductSubCategory> getOneSubCategoryById(String id) {
        return productSubCategoryRepository.findOneById(id);
    }

    @Override
    public <S extends ProductSubCategory> ProductSubCategory updateSubCategory(ProductSubCategory productSubCategory) {
        return productSubCategoryRepository.save(productSubCategory);
    }

    @Override
    public <S extends ProductSubSubCategory> ProductSubSubCategory saveSubSubCategory(ProductSubSubCategory productSubSubCategory) {
        return productSubSubCategoryRepository.save(productSubSubCategory);
    }

    @Override
    public void deleteSubSubCategoryById(String id) {
        productSubSubCategoryRepository.deleteById(id);
    }

    @Override
    public <S extends ProductSubSubCategory> Optional<ProductSubSubCategory> getOneSubSubCategoryById(String id) {
        return productSubSubCategoryRepository.findOneById(id);
    }

    @Override
    public <S extends ProductSubSubCategory> ProductSubSubCategory updateSubSubCategory(ProductSubSubCategory productSubSubCategory) {
        return productSubSubCategoryRepository.save(productSubSubCategory);
    }

    @Override
    public Optional<ProductCategory> getOneCategoryByName(String name) {
        return productCategoryRepository.findOneByName(name);
    }

    @Override
    public Optional<ProductSubCategory> getOneSubCategoryByCategoryIdAndName(String categoryId, String name) {
        return productSubCategoryRepository.findOneByCategoryIdAndName(categoryId, name);
    }

    @Override
    public Optional<ProductSubSubCategory> getOneSubSubCategoryByCategoryIdAndSubCategoryIdAndName(String categoryId, String subCategoryId, String name) {
        return productSubSubCategoryRepository.findOneByCategoryIdAndSubCategoryIdAndName(categoryId, subCategoryId, name);
    }
}
