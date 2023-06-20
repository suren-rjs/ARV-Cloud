package com.api.arv.controller;

import com.api.arv.helper.GlobalCommonService;
import com.api.arv.model.document.ProductCategory;
import com.api.arv.model.document.ProductSubCategory;
import com.api.arv.model.document.ProductSubSubCategory;
import com.api.arv.repository.crud.ProductCategoryRepository;
import com.api.arv.repository.crud.ProductSubCategoryRepository;
import com.api.arv.repository.crud.ProductSubSubCategoryRepository;
import com.api.arv.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    public ProductSubSubCategoryRepository productSubSubCategoryRepository;

    @GetMapping("/productCategories")
    public ResponseEntity<?> getCategories(@RequestParam("keyword") String keyword) {
        if (keyword.replaceAll(" ", "").isEmpty()) {
            return globalCommonService.getResponseOfObject(Optional.of(productCategoryRepository.findAll()), HttpStatus.OK);
        }
        return globalCommonService.getResponseOfObject(Optional.of(productCategoryRepository.findByNameContainingIgnoreCase(keyword)), HttpStatus.OK);
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
        if (productCategoryRepository.findOneById(id).isEmpty()) {
            return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
        }
        message = "Category deleted";
        status = HttpStatus.OK;
        productCategoryService.deleteCategoryById(id);
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @GetMapping("/productSubCategories")
    public ResponseEntity<?> getSubCategories(@RequestParam String categoryId, @RequestParam String keyword) {
        if (keyword.replaceAll(" ", "").isEmpty()) {
            return globalCommonService.getResponseOfObject(Optional.of(productSubCategoryRepository.findAllByCategoryId(categoryId)), HttpStatus.OK);
        }
        return globalCommonService.getResponseOfObject(Optional.of(productSubCategoryRepository.findOneByCategoryIdAndNameContainingIgnoreCase(categoryId, keyword)), HttpStatus.OK);
    }

    @GetMapping("/productSubCategories/{id}")
    public ResponseEntity<?> getSubCategoryById(@PathVariable String id) {
        String message = "Invalid sub category id";
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProductSubCategory subCategory = productCategoryService.getOneSubCategoryById(id).orElse(null);
        if (subCategory == null) {
            return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
        }
        return globalCommonService.getResponseOfObject(Optional.of(subCategory), HttpStatus.OK);
    }

    @PostMapping("/productSubCategories")
    public ResponseEntity<?> saveSubCategory(@RequestBody ProductSubCategory productSubCategory) {
        String message = "new sub category created";
        HttpStatus status = HttpStatus.OK;
        productCategoryService.saveSubCategory(productSubCategory);
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @PutMapping("/productSubCategories")
    public ResponseEntity<?> updateSubCategoryById(@RequestBody ProductSubCategory productSubCategory) {
        String message = "Invalid sub category id";
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        if (productSubCategory.getId() == null) {
            return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
        }
        message = "Sub Category updated";
        status = HttpStatus.OK;
        productCategoryService.saveSubCategory(productSubCategory);
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @DeleteMapping("/productSubCategories/{id}")
    public ResponseEntity<?> deleteSubCategoryById(@PathVariable("id") String id) {
        String message = "Invalid id";
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        if (productSubCategoryRepository.findOneById(id).isEmpty()) {
            return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
        }
        message = "Sub Category deleted";
        status = HttpStatus.OK;
        productCategoryService.deleteSubCategoryById(id);
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @GetMapping("/productSubSubCategories")
    public ResponseEntity<?> getSubSubCategories(@RequestParam String categoryId, @RequestParam String subCategoryId, @RequestParam String keyword) {
        if (keyword.replaceAll(" ", "").isEmpty()) {
            globalCommonService.getResponseOfObject(Optional.of(productSubSubCategoryRepository.findAllByCategoryIdAndSubCategoryId(categoryId, subCategoryId)), HttpStatus.OK);
        }
        return globalCommonService.getResponseOfObject(Optional.of(productSubSubCategoryRepository.findOneByCategoryIdAndSubCategoryIdAndNameContainingIgnoreCase(categoryId, subCategoryId, keyword)), HttpStatus.OK);
    }

    @GetMapping("/productSubSubCategories/{id}")
    public ResponseEntity<?> getSubSubCategoryById(@PathVariable String id) {
        String message = "Invalid sub-sub category id";
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProductSubSubCategory productSubSubCategory = productCategoryService.getOneSubSubCategoryById(id).orElse(null);
        if (productSubSubCategory == null) {
            return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
        }
        return globalCommonService.getResponseOfObject(Optional.of(productSubSubCategory), HttpStatus.OK);
    }

    @PostMapping("/productSubSubCategories")
    public ResponseEntity<?> saveSubSubCategory(@RequestBody ProductSubSubCategory productSubSubCategory) {
        String message = "new sub-sub category created";
        HttpStatus status = HttpStatus.OK;
        productCategoryService.saveSubSubCategory(productSubSubCategory);
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @PutMapping("/productSubSubCategories")
    public ResponseEntity<?> updateSubSubCategoryById(@RequestBody ProductSubSubCategory productSubSubCategory) {
        String message = "Invalid sub-sub category id";
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        if (productSubSubCategory.getId() == null) {
            return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
        }
        message = "Category updated";
        status = HttpStatus.OK;
        productCategoryService.saveSubSubCategory(productSubSubCategory);
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }

    @DeleteMapping("/productSubSubCategories/{id}")
    public ResponseEntity<?> deleteSubSubCategoryById(@PathVariable("id") String id) {
        String message = "Invalid sub-sub category id";
        HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
        if (productSubSubCategoryRepository.findOneById(id).isEmpty()) {
            return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
        }
        message = "Sub-sub Category deleted";
        status = HttpStatus.OK;
        productCategoryService.deleteSubSubCategoryById(id);
        return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
    }
}
