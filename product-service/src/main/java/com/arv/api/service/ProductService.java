package com.arv.api.service;

import com.arv.api.model.document.ProductCategory;
import com.arv.api.model.document.ProductItem;
import com.arv.api.model.document.ProductSubCategory;
import com.arv.api.model.document.ProductSubSubCategory;
import com.arv.api.model.dto.request.product.ProductItemDTO;
import com.arv.api.model.dto.response.ProductList;
import com.arv.api.repository.crud.ProductItemRepository;
import com.arv.api.repository.data.ProductDtoRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Service
public class ProductService implements ProductDtoRepository {
    @Value("${product.pageSize}")
    private int PAGE_COUNT;
    Logger logger = LoggerFactory.getLogger(ProductService.class);

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
            if (productItem == null) throw new Exception("Product not found");
            productItem = modelMapper.map(updatedDetails, ProductItem.class);
            productItemRepository.save(productItem);
        } catch (Exception e) {
            throw new Exception("Unable to update");
        }
    }

    @Override
    public <S extends ProductItem> Optional<S> getById(String id) throws Exception {
        if (productItemRepository.existsById(id)) {
            return productItemRepository.findOneById(id);
        } else {
            throw new Exception("Product not exist");
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

    @Override
    public ProductList search(String keyword, double priceFrom, double priceTo, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_COUNT);

        // Perform the repository queries with pagination
        Page<ProductItem> productsByNameOrDescription = productItemRepository.findByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable);
        Page<ProductItem> productsBySellingPrice = productItemRepository.findBySellingPriceBetween(priceFrom - 1, priceTo + 1, pageable);
        Page<ProductItem> productsByMrpPrice = productItemRepository.findByMrpPriceBetween(priceFrom - 1, priceTo + 1, pageable);
        Page<ProductItem> productsByMinSellingPrice = productItemRepository.findBySellingPriceGreaterThanEqual(priceFrom - 1, pageable);
        Page<ProductItem> productsByMinMrpPrice = productItemRepository.findByMrpPriceGreaterThanEqual(priceFrom - 1, pageable);
        Page<ProductItem> productsByMaxSellingPrice = productItemRepository.findBySellingPriceLessThanEqual(priceTo + 1, pageable);
        Page<ProductItem> productsByMaxMrpPrice = productItemRepository.findByMrpPriceLessThanEqual(priceTo + 1, pageable);

        // Get the content of the pages as filtered products
        List<ProductItem> filteredProducts = new ArrayList<>(productsByNameOrDescription.getContent());

        ProductList productList = new ProductList(filteredProducts, page, productsByNameOrDescription.getTotalElements(), getTotalPage(productsByNameOrDescription));

        if (priceFrom == 0 && priceTo == 0) {
            return productList;
        } else if (priceFrom == 0) {
            filteredProducts.retainAll(productsByMaxSellingPrice.getContent());
            filteredProducts.retainAll(productsByMaxMrpPrice.getContent());
        } else if (priceTo == 0) {
            filteredProducts.retainAll(productsByMinSellingPrice.getContent());
            filteredProducts.retainAll(productsByMinMrpPrice.getContent());
        } else {
            filteredProducts.retainAll(productsBySellingPrice.getContent());
            filteredProducts.retainAll(productsByMrpPrice.getContent());
        }

        int totalPage = (filteredProducts.size() / PAGE_COUNT);
        totalPage = totalPage <= 1 ? 0 : (totalPage - 1);

        productList = new ProductList(filteredProducts, page, filteredProducts.size(), totalPage);

        return productList;
    }

    public ProductList findProductByIdsIn(String[] ids, int page) {
        Page<ProductItem> products = productItemRepository.findAllByIdIn(ids, PageRequest.of(page, PAGE_COUNT));
        // Get the content of the pages as filtered products
        List<ProductItem> filteredProducts = new ArrayList<>(products.getContent());

        return new ProductList(filteredProducts, page, products.getTotalElements(), getTotalPage(products));
    }

    private static int getTotalPage(Page<ProductItem> products) {
        int totalPage = products.getTotalPages();
        totalPage = totalPage <= 1 ? 0 : (totalPage - 1);
        return totalPage;
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
