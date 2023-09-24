package com.arv.api.service;

import com.arv.api.model.document.ProductCategory;
import com.arv.api.model.document.ProductSubCategory;
import com.arv.api.model.dto.request.ProductCategoryDTO;
import com.arv.api.model.dto.request.ProductSubCategoryDTO;
import com.arv.api.repository.crud.ProductCategoryRepository;
import com.arv.api.repository.crud.ProductSubCategoryRepository;
import com.arv.api.repository.data.ProductCategoryDtoRepository;
import com.arv.api.service.utils.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.util.Optional;

@SuppressWarnings("unused")
@Service
public class ProductCategoryService implements ProductCategoryDtoRepository {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductSubCategoryRepository productSubCategoryRepository;

    @Autowired
    protected StorageService service;

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
    public <S extends ProductCategory> ProductCategory updateCategory(ProductCategoryDTO productCategory) {
        ProductCategory category = productCategoryRepository.findOneById(productCategory.getId()).orElseThrow(() -> new NotFoundException("Invalid Id"));
        category.setName(productCategory.getName());
        if (productCategory.getImage() != null) {
            String id = productCategory.getId();
            category.setImage(uploadFile(convertBytesToMux(productCategory.getImage(), id, "image/jpeg"), id));
        }
        return productCategoryRepository.save(category);
    }

    @Override
    public <S extends ProductSubCategory> ProductSubCategory saveSubCategories(ProductSubCategoryDTO productSubCategory) {
        Optional<ProductSubCategory> subCategory = productSubCategoryRepository.findOneById(productSubCategory.getId());
        if (subCategory.isPresent()) {
            return updateSubCategories(productSubCategory);
        }
        {
            ProductSubCategory newProductSubCategory = new ProductSubCategory();
            newProductSubCategory.setName(productSubCategory.getName());
            newProductSubCategory.setCategoryId(productSubCategory.getCategoryId());
            String id = productSubCategoryRepository.save(newProductSubCategory).getId();
            if (productSubCategory.getImage() != null) {
                newProductSubCategory.setImage(uploadFile(convertBytesToMux(productSubCategory.getImage(), id, "image/jpeg"), id));
            }
            return productSubCategoryRepository.save(newProductSubCategory);
        }
    }

    @Override
    public void deleteSubCategoriesById(String id) {
        productSubCategoryRepository.deleteById(id);
    }

    @Override
    public <S extends ProductSubCategory> Optional<ProductSubCategory> getOneSubCategoriesById(String id) {
        return productSubCategoryRepository.findOneById(id);
    }

    @Override
    public ProductSubCategory updateSubCategories(ProductSubCategoryDTO productSubCategory) {
        ProductSubCategory subCategory = productSubCategoryRepository.findOneById(productSubCategory.getId()).orElseThrow(() -> new NotFoundException("Invalid Id"));
        subCategory.setName(productSubCategory.getName());
        if (productSubCategory.getImage() != null) {
            String id = productSubCategory.getId();
            subCategory.setImage(uploadFile(convertBytesToMux(productSubCategory.getImage(), id, "image/jpeg"), id));
        }
        return productSubCategoryRepository.save(subCategory);
    }

    @Override
    public Optional<ProductCategory> findOneCategoryByNameOrId(String name) {
        return productCategoryRepository.findOneByNameOrId(name, name);
    }

    @Override
    public Optional<ProductCategory> getOneCategoryByName(String name) {
        return productCategoryRepository.findOneByName(name);
    }

    @Override
    public Optional<ProductSubCategory> getOneByCategoryIdAndName(String categoryId, String name) {
        return productSubCategoryRepository.findOneByCategoryIdAndName(categoryId, name);
    }

    @Override
    public Optional<ProductSubCategory> getOneByCategoryIdAndId(String categoryId, String id) {
        return productSubCategoryRepository.findOneByCategoryIdAndId(categoryId, id);
    }

    private String uploadFile(MultipartFile file, String id) {
        if (file == null) return "";
        return service.uploadFile(file, id);
    }

    public static MultipartFile convertBytesToMux(byte[] data, String filename, String contentType) {
        return new MockMultipartFile(filename, filename, contentType, data);
    }

    @Async
    private void deleteFile(String fileName) {
        service.deleteFile(fileName);
    }
}
