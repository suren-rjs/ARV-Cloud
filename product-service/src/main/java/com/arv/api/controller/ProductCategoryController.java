package com.arv.api.controller;

import com.arv.api.helper.GlobalCommonService;
import com.arv.api.model.document.ProductCategory;
import com.arv.api.model.document.ProductSubCategory;
import com.arv.api.model.dto.response.CategoryList;
import com.arv.api.model.dto.response.SubCategoryList;
import com.arv.api.repository.crud.ProductCategoryRepository;
import com.arv.api.repository.crud.ProductSubCategoryRepository;
import com.arv.api.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@SuppressWarnings("All")
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductCategoryController {
    @Autowired
    public GlobalCommonService globalCommonService;

    @Autowired
    public ProductCategoryService productCategoryService;

    @Autowired
    public ProductCategoryRepository productCategoryRepository;
    @Autowired
    public ProductSubCategoryRepository productSubCategoryRepository;

    @GetMapping("/productCategories")
    public ResponseEntity<?> getCategories(@RequestParam(required = false, defaultValue = "") String keyword, @RequestParam(required = false, defaultValue = "0") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 15);
        Page<ProductCategory> page;
        if (keyword.replaceAll(" ", "").isEmpty())
            page = productCategoryRepository.findAll(pageable);
        else
            page = productCategoryRepository.findByNameContainingIgnoreCase(keyword, pageable);
        CategoryList categoryList = new CategoryList(page.getContent(), pageNumber, page.getTotalElements(), globalCommonService.getTotalPage(page.getTotalPages()));
        return globalCommonService.getResponseOfObject(Optional.of(categoryList), HttpStatus.OK);
    }

    @GetMapping("/productCategories/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable String id) {
        String message = "Invalid id";
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProductCategory category = productCategoryService.getOneCategoryById(id).orElse(null);
        if (category == null) {
            return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
        }
        return globalCommonService.getResponseOfObject(Optional.of(category), HttpStatus.OK);
    }

    @PostMapping("/productCategories")
    public ResponseEntity<?> saveCategory(@RequestBody ProductCategory productCategory) {
        String message = "new category created";
        HttpStatus status = HttpStatus.OK;
        productCategoryService.saveCategory(productCategory);
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @PutMapping("/productCategories")
    public ResponseEntity<?> updateCategoryById(@RequestBody ProductCategory productCategory) {
        String message = "Invalid id";
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        if (productCategory.getId() == null) {
            return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
        }
        message = "Category updated";
        status = HttpStatus.OK;
        productCategoryService.saveCategory(productCategory);
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @DeleteMapping("/productCategories/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable("id") String id) {
        String message = "Invalid id";
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        if (!productCategoryRepository.existsById(id)) {
            return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
        }
        message = "Category deleted";
        status = HttpStatus.OK;
        productCategoryService.deleteCategoryById(id);
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @GetMapping("/productSubCategories")
    public ResponseEntity<?> getSubSubCategories(@RequestParam String categoryId, @RequestParam(required = false, defaultValue = "") String keyword, @RequestParam(required = false, defaultValue = "0") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 15);
        Page<ProductSubCategory> page;
        if (keyword.replaceAll(" ", "").isEmpty())
            page = productSubCategoryRepository.findAllByCategoryId(categoryId, pageable);
        else
            page = productSubCategoryRepository.findAllByCategoryIdAndNameContainingIgnoreCase(categoryId, keyword, pageable);

        SubCategoryList subCategoryList = new SubCategoryList(page.getContent(), pageNumber, page.getTotalElements(), globalCommonService.getTotalPage(page.getTotalPages()));
        return globalCommonService.getResponseOfObject(Optional.of(subCategoryList), HttpStatus.OK);
    }

    @GetMapping("/productSubCategories/{id}")
    public ResponseEntity<?> getSubCategoriesById(@PathVariable String id) {
        String message = "Invalid sub category id";
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProductSubCategory productSubCategory = productCategoryService.getOneSubCategoriesById(id).orElse(null);
        if (productSubCategory == null) {
            return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
        }
        return globalCommonService.getResponseOfObject(Optional.of(productSubCategory), HttpStatus.OK);
    }

    @PostMapping("/productSubCategories")
    public ResponseEntity<?> saveSubCategories(@RequestBody ProductSubCategory productSubCategory) {
        String message = "new sub category created";
        HttpStatus status = HttpStatus.OK;
        productCategoryService.saveSubCategories(productSubCategory);
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @PutMapping("/productSubCategories")
    public ResponseEntity<?> updateSubCategoriesById(@RequestBody ProductSubCategory productSubCategory) {
        String message = "Invalid sub category id";
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        if (productSubCategory.getId() == null) {
            return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
        }
        message = "Category updated";
        status = HttpStatus.OK;
        productCategoryService.saveSubCategories(productSubCategory);
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @DeleteMapping("/productSubCategories/{id}")
    public ResponseEntity<?> deleteSubCategoriesById(@PathVariable("id") String id) {
        String message = "Invalid sub category id";
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        if (!productSubCategoryRepository.findOneById(id).isPresent()) {
            return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
        }
        message = "Sub Category deleted";
        status = HttpStatus.OK;
        productCategoryService.deleteSubCategoriesById(id);
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }
}
