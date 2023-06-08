package com.api.arv.service;

import com.api.arv.model.document.ProductCategory;
import com.api.arv.model.document.ProductItem;
import com.api.arv.model.document.ProductSubCategory;
import com.api.arv.model.document.ProductSubSubCategory;
import com.api.arv.model.dto.request.product.ProductItemDTO;
import com.api.arv.repository.crud.ProductItemRepository;
import com.api.arv.repository.data.ProductDtoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Service
public class ProductService implements ProductDtoRepository {

    @Autowired
    protected ProductCategoryService productCategoryService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductItemRepository productItemRepository;

    @Override
    public <S extends ProductItem> void save(ProductItemDTO productItemDto) {
        ProductItem productItem = modelMapper.map(productItemDto, ProductItem.class);

        String categoryName = productItemDto.getCategoryId();
        String subCategoryName = productItemDto.getSubCategoryId() == null ? categoryName : productItemDto.getSubCategoryId();
        String subSubCategoryName = productItemDto.getSubSubCategoryId() == null ? subCategoryName : productItemDto.getSubSubCategoryId();

        String categoryId = getCategoryId(categoryName);
        String subCategoryId = getSubCategoryId(categoryId, subCategoryName);
        String subSubCategoryId = getSubSubCategoryId(categoryId, subCategoryId, subSubCategoryName);

        productItem.setCategoryId(categoryId);
        productItem.setSubCategoryId(subCategoryId);
        productItem.setSubSubCategoryId(subSubCategoryId);

        productItemRepository.save(productItem);
    }

    @Override
    public void deleteById(String id) {
        try {
            getById(id);
            productItemRepository.deleteById(id);
        } catch (Exception ignored) {
        }
    }

    @Override
    public <S extends ProductItem> void updateById(ProductItemDTO updatedDetails) throws Exception {
        try {
            ProductItem productItem = getById(updatedDetails.getId()).orElse(null);
            if (productItem == null) throw new NotFoundException("Product not found");
            productItem = modelMapper.map(updatedDetails, ProductItem.class);
            productItemRepository.save(productItem);
        } catch (Exception e) {
            throw new Exception("Unable to update");
        }
    }

    @Override
    public <S extends ProductItem> Optional<S> getById(String id) throws NotFoundException {
        Optional<ProductItem> productItem = productItemRepository.findOneById(id);
        if (productItem.isPresent()) {
            return productItemRepository.findOneById(id);
        } else {
            throw new NotFoundException("Product not exist");
        }
    }

    @Override
    public <S extends ProductItem> List<ProductItem> getAllProducts() {
        return productItemRepository.findAll();
    }

    @Override
    public void saveAll(List<ProductItemDTO> productItemDTOList) {
        for (ProductItemDTO productItemDTO : productItemDTOList) {
            String categoryName = productItemDTO.getCategoryId();
            String subCategoryName = productItemDTO.getSubCategoryId();
            String subSubCategoryName = productItemDTO.getSubSubCategoryId();

            String categoryId = getCategoryId(categoryName);
            String subCategoryId = getSubCategoryId(categoryId, subCategoryName);
            String subSubCategoryId = getSubSubCategoryId(categoryId, subCategoryId, subSubCategoryName);

            productItemDTO.setCategoryId(categoryId);
            productItemDTO.setSubCategoryId(subCategoryId);
            productItemDTO.setSubSubCategoryId(subSubCategoryId);
        }
        Iterable<ProductItem> iterableProductItems = () -> productItemDTOList.stream().map(productItemDTO -> modelMapper.map(productItemDTO, ProductItem.class)).iterator();
        productItemRepository.saveAll(iterableProductItems);
    }

    private String getCategoryId(String name) {
        Optional<ProductCategory> productCategory = productCategoryService.getOneCategoryByName(name);

        if (productCategory.isPresent()) return productCategory.get().getId();

        ProductCategory newProductCategory = new ProductCategory();
        newProductCategory.setName(name);
        newProductCategory = productCategoryService.saveCategory(newProductCategory);
        return newProductCategory.getId();
    }

    private String getSubCategoryId(String categoryId, String name) {
        Optional<ProductSubCategory> productSubCategory = productCategoryService.getOneSubCategoryByCategoryIdAndName(categoryId, name);

        if (productSubCategory.isPresent()) return productSubCategory.get().getId();

        ProductSubCategory subCategory = new ProductSubCategory();
        subCategory.setName(name);
        subCategory.setCategoryId(categoryId);
        subCategory = productCategoryService.saveSubCategory(subCategory);
        return subCategory.getId();
    }

    private String getSubSubCategoryId(String categoryId, String subCategoryId, String name) {
        Optional<ProductSubSubCategory> productSubSubCategory = productCategoryService.getOneSubSubCategoryByCategoryIdAndSubCategoryIdAndName(categoryId, subCategoryId, name);

        if (productSubSubCategory.isPresent()) return productSubSubCategory.get().getId();

        ProductSubSubCategory subSubCategory = new ProductSubSubCategory();
        subSubCategory.setName(name);
        subSubCategory.setCategoryId(categoryId);
        subSubCategory.setSubCategoryId(subCategoryId);
        subSubCategory = productCategoryService.saveSubSubCategory(subSubCategory);
        return subSubCategory.getId();
    }
}
