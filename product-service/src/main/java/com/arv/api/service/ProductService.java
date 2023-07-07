package com.arv.api.service;

import com.arv.api.model.document.ProductCategory;
import com.arv.api.model.document.ProductItem;
import com.arv.api.model.document.ProductSubCategory;
import com.arv.api.model.dto.request.product.ProductItemDTO;
import com.arv.api.model.dto.response.ProductList;
import com.arv.api.repository.crud.ProductCategoryRepository;
import com.arv.api.repository.crud.ProductItemRepository;
import com.arv.api.repository.data.ProductDtoRepository;
import com.arv.api.service.utils.StorageService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private StorageService service;

    @Override
    public <S extends ProductItem> void save(ProductItemDTO productItemDto) {
        ProductItem productItem = modelMapper.map(productItemDto, ProductItem.class);

        String categoryName = productItemDto.getCategoryId();
        String subCategoryName = productItemDto.getSubCategoryTags() == null ? categoryName : productItemDto.getSubCategoryTags();
        String subSubCategoryName = productItemDto.getSubSubCategoryId() == null ? subCategoryName : productItemDto.getSubSubCategoryId();

        ProductCategory productCategory = getCategoryId(categoryName);
        String productSubCategory = productItem.getSubCategoryTags();
        ProductSubCategory productSubSubCategory = getSubSubCategoryId(productCategory.getId(), subSubCategoryName);

        productItem.setCategoryId(productCategory.getId());
        productItem.setSubCategoryTags(productSubCategory);
        productItem.setSubSubCategoryId(productSubSubCategory.getId());

        productItem.setProductCategory(productCategory);
        productItem.setProductSubCategory(productSubSubCategory);

        productItem.setImage(uploadFile(productItemDto.getImage(), productItemDto.getProductName().toLowerCase().replaceAll(" ", "-")));
        productItem.setTags(productItemDto.getTags());

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
            ProductItem productItem = getById(updatedDetails.getId());
            if (productItem == null) throw new Exception("Product not found");
            productItem = modelMapper.map(updatedDetails, ProductItem.class);
            productItemRepository.save(productItem);
        } catch (Exception e) {
            throw new Exception("Unable to update");
        }
    }

    @Override
    public <S extends ProductItem> ProductItem getById(String id) throws Exception {
        if (productItemRepository.existsById(id)) {
            return productItemRepository.findOneById(id).orElse(null);
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
            String subCategoryName = productItemDTO.getSubCategoryTags();
            String subSubCategoryName = productItemDTO.getSubSubCategoryId();

            ProductCategory productCategory = getCategoryId(categoryName);
            ProductSubCategory productSubCategory = getSubSubCategoryId(productCategory.getId(), subSubCategoryName);

            productItemDTO.setCategoryId(productCategory.getId());
            productItemDTO.setCategoryId(productItemDTO.getSubCategoryTags());
            productItemDTO.setSubSubCategoryId(productSubCategory.getId());

            productItemDTO.setProductCategory(productCategory);
            productItemDTO.setProductSubCategory(productSubCategory);
        }

        Iterable<ProductItem> iterableProductItems = () -> productItemDTOList.stream().map(productItemDTO -> {
            ProductItem productItem = modelMapper.map(productItemDTO, ProductItem.class);
            if (productItemDTO.getImage() != null)
                productItem.setImage(uploadFile(productItemDTO.getImage(), productItemDTO.getId()));
            return productItem;
        }).iterator();

        productItemRepository.saveAll(iterableProductItems);
    }

    @Override
    public ProductList search(String categoryId, String keyword, double priceFrom, double priceTo, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_COUNT);

        // Perform the repository queries with pagination
        Page<ProductItem> productsByNameOrDescription = productItemRepository.findByCategoryIdContainingIgnoreCaseOrProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(categoryId, keyword, keyword, pageable);
        Page<ProductItem> productsBySellingPrice = productItemRepository.findByCategoryIdContainingIgnoreCaseAndSellingPriceBetween(categoryId, priceFrom - 1, priceTo + 1, pageable);
        Page<ProductItem> productsByMrpPrice = productItemRepository.findByCategoryIdContainingIgnoreCaseAndMrpPriceBetween(categoryId, priceFrom - 1, priceTo + 1, pageable);
        Page<ProductItem> productsByMinSellingPrice = productItemRepository.findByCategoryIdContainingIgnoreCaseAndSellingPriceGreaterThanEqual(categoryId, priceFrom - 1, pageable);
        Page<ProductItem> productsByMinMrpPrice = productItemRepository.findByCategoryIdContainingIgnoreCaseAndMrpPriceGreaterThanEqual(categoryId, priceFrom - 1, pageable);
        Page<ProductItem> productsByMaxSellingPrice = productItemRepository.findByCategoryIdContainingIgnoreCaseAndSellingPriceLessThanEqual(categoryId, priceTo + 1, pageable);
        Page<ProductItem> productsByMaxMrpPrice = productItemRepository.findByCategoryIdContainingIgnoreCaseAndMrpPriceLessThanEqual(categoryId, priceTo + 1, pageable);

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

    private ProductCategory getCategoryId(String name) {
        Optional<ProductCategory> productCategory = productCategoryService.getOneCategoryByName(name);

        if (productCategory.isPresent()) return productCategory.get();

        ProductCategory newProductCategory = new ProductCategory();
        newProductCategory.setName(name);
        newProductCategory = productCategoryService.saveCategory(newProductCategory);
        return newProductCategory;
    }

    private ProductSubCategory getSubSubCategoryId(String categoryId, String name) {
        Optional<ProductSubCategory> productSubSubCategory = productCategoryService.getOneSubSubCategoryByCategoryIdAndName(categoryId, name);

        if (productSubSubCategory.isPresent()) return productSubSubCategory.get();

        ProductSubCategory subSubCategory = new ProductSubCategory();
        subSubCategory.setName(name);
        subSubCategory.setCategoryId(categoryId);
        return productCategoryService.saveSubSubCategory(subSubCategory);
    }

    private String uploadFile(MultipartFile file, String id) {
        return service.uploadFile(file, id);
    }

    public ResponseEntity<ByteArrayResource> downloadFile(String fileName) {
        byte[] data = service.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData("inline", fileName);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .headers(headers)
                .body(resource);
    }

    private void deleteFile(String fileName) {
        service.deleteFile(fileName);
    }
}
