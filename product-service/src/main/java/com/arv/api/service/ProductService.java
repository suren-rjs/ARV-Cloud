package com.arv.api.service;

import com.arv.api.helper.GlobalCommonService;
import com.arv.api.model.document.ProductCategory;
import com.arv.api.model.document.ProductItem;
import com.arv.api.model.document.ProductSubCategory;
import com.arv.api.model.dto.request.ProductItemDTO;
import com.arv.api.model.dto.response.ProductList;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

@SuppressWarnings("unused")
@Service
public class ProductService implements ProductDtoRepository {
    @Value("${product.pageSize}")
    protected int PAGE_COUNT;
    Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    protected ProductCategoryService productCategoryService;
    @Autowired
    protected ModelMapper modelMapper;
    @Autowired
    protected ProductItemRepository productItemRepository;

    @Autowired
    protected GlobalCommonService globalCommonService;

    @Autowired
    protected StorageService service;

    @Override
    public <S extends ProductItem> void save(ProductItemDTO productItemDto) {
        String id = productItemDto.getStoreId() + productItemDto.getProductName().toLowerCase().replaceAll(" ", "-");

        ProductItem productItem = new ProductItem();
        productItem.setProductName(productItemDto.getProductName());
        productItem.setDescription(productItemDto.getDescription());
        productItem.setSubCategoryTags(productItemDto.getSubCategoryTags());
        productItem.setSellingPrice(productItemDto.getSellingPrice());
        productItem.setMrpPrice(productItemDto.getMrpPrice());
        productItem.setQuantity(productItemDto.getQuantity());
        productItem.setStock(productItemDto.getStock());
        productItem.setDiscount(productItemDto.getDiscount());
        productItem.setTags(productItemDto.getTags());

        String categoryNameOrId = productItemDto.getCategoryNameOrId();
        String subCategoryNameOrId = productItemDto.getSubCategoryNameOrId() == null ? categoryNameOrId : productItemDto.getSubCategoryNameOrId();

        ProductCategory productCategory = getCategoryId(categoryNameOrId);
        ProductSubCategory productSubCategory = getSubCategoryId(productCategory.getId(), subCategoryNameOrId);
        String subCategoryTags = productItem.getSubCategoryTags();

        productItem.setCategoryId(productCategory.getId());
        productItem.setSubCategoryId(productSubCategory.getId());
        productItem.setSubCategoryTags(subCategoryTags);

        productItem.setProductCategory(productCategory);
        productItem.setProductSubCategory(productSubCategory);

        productItem.setImageUri(uploadFile(convertBytesToMux(productItemDto.getImage(), id, "image/jpeg"), id));
        productItem.setTags(productItemDto.getTags());
        productItem.setId(id);

        productItemRepository.save(productItem);
    }

    @Override
    public void deleteById(String id) {
        try {
            getById(id);
            productItemRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        try {
            deleteFile(id);
        } catch (Exception ignored) {
        }
    }

    @Override
    public <S extends ProductItem> void updateById(ProductItemDTO updatedDetails) throws Exception {
        try {
            ProductItem productItem = getById(updatedDetails.getId());
            if (productItem == null) throw new Exception("Product not found");
            productItem.setProductName(updatedDetails.getProductName());
            productItem.setDescription(updatedDetails.getDescription());
            productItem.setSubCategoryTags(updatedDetails.getSubCategoryTags());
            productItem.setSellingPrice(updatedDetails.getSellingPrice());
            productItem.setMrpPrice(updatedDetails.getMrpPrice());
            productItem.setQuantity(updatedDetails.getQuantity());
            productItem.setStock(updatedDetails.getStock());
            productItem.setDiscount(updatedDetails.getDiscount());
            productItem.setTags(updatedDetails.getTags());
            productItem.setScannerId(updatedDetails.getScannerId());
            productItem.setBrand(updatedDetails.getBrand());
            productItem.setProductVariation(updatedDetails.getProductVariation());


            String categoryNameOrId = updatedDetails.getCategoryNameOrId();
            String subCategoryNameOrId = updatedDetails.getSubCategoryNameOrId() == null ? categoryNameOrId : updatedDetails.getSubCategoryNameOrId();

            ProductCategory productCategory = getCategoryId(categoryNameOrId);
            ProductSubCategory productSubCategory = getSubCategoryId(productCategory.getId(), subCategoryNameOrId);

            productItem.setCategoryId(productCategory.getId());
            productItem.setSubCategoryId(productSubCategory.getId());

            productItem.setProductCategory(productCategory);
            productItem.setProductSubCategory(productSubCategory);

            if (updatedDetails.getImage() != null) {
                productItem.setImageUri(uploadFile(convertBytesToMux(updatedDetails.getImage(), updatedDetails.getId(), "image/jpeg"), updatedDetails.getId()));
            }

            productItemRepository.save(productItem);
        } catch (Exception e) {
            throw new Exception("Unable to update");
        }
    }

    @Override
    public <S extends ProductItem> ProductItem getById(String id) throws Exception {
        return productItemRepository.findOneById(id).orElseThrow(() -> new Exception("Product not exist"));
    }

    @Override
    public <S extends ProductItem> List<ProductItem> getAllProducts() {
        return productItemRepository.findAll();
    }

    @Override
    public void saveAll(List<ProductItemDTO> productItemDTOList) {
        for (ProductItemDTO productItemDTO : productItemDTOList) {
            String categoryNameOrId = productItemDTO.getCategoryNameOrId();
            String subCategoryNameOrId = productItemDTO.getSubCategoryNameOrId();

            ProductCategory productCategory = getCategoryId(categoryNameOrId);
            ProductSubCategory productSubCategory = getSubCategoryId(productCategory.getId(), subCategoryNameOrId);

            productItemDTO.setCategoryNameOrId(productCategory.getId());
            productItemDTO.setSubCategoryTags(productItemDTO.getSubCategoryTags());
            productItemDTO.setSubCategoryNameOrId(productSubCategory.getId());

            productItemDTO.setProductCategory(productCategory);
            productItemDTO.setProductSubCategory(productSubCategory);
        }

        Iterable<ProductItem> iterableProductItems = () -> productItemDTOList.stream().map(productItemDTO -> {
            String id = productItemDTO.getStoreId() + productItemDTO.getProductName().toLowerCase().replaceAll(" ", "-");

            ProductItem productItem = new ProductItem();
            productItem.setProductName(productItemDTO.getProductName());
            productItem.setDescription(productItemDTO.getDescription());
            productItem.setSubCategoryTags(productItemDTO.getSubCategoryTags());
            productItem.setSellingPrice(productItemDTO.getSellingPrice());
            productItem.setMrpPrice(productItemDTO.getMrpPrice());
            productItem.setQuantity(productItemDTO.getQuantity());
            productItem.setStock(productItemDTO.getStock());
            productItem.setDiscount(productItemDTO.getDiscount());
            productItem.setTags(productItemDTO.getTags());
            productItem.setScannerId(productItemDTO.getScannerId());
            productItem.setBrand(productItemDTO.getBrand());
            productItem.setProductVariation(productItemDTO.getProductVariation());
            productItem.setStoreId(productItemDTO.getStoreId());
            productItem.setMajorCategoryId(productItemDTO.getMajorCategoryId());

            String categoryNameOrId = productItemDTO.getCategoryNameOrId();
            String subCategoryNameOrId = productItemDTO.getSubCategoryNameOrId() == null ? categoryNameOrId : productItemDTO.getSubCategoryNameOrId();

            ProductCategory productCategory = getCategoryId(categoryNameOrId);
            ProductSubCategory productSubCategory = getSubCategoryId(productCategory.getId(), subCategoryNameOrId);

            productItem.setCategoryId(productCategory.getId());
            productItem.setSubCategoryId(productSubCategory.getId());

            productItem.setProductCategory(productCategory);
            productItem.setProductSubCategory(productSubCategory);
            productItem.setId(id);
            if (productItemDTO.getImageFile() != null) {
                productItem.setImageUri(uploadFile(productItemDTO.getImageFile(), id));
            }
            return productItem;
        }).iterator();


        System.out.println("Product Images Upload Completed");
        productItemRepository.saveAll(iterableProductItems);
        System.out.println("Product Upload Completed");
    }

    @Override
    public ProductList search(String majorCategory, String categoryId, String subCategoryId, String keyword, double priceFrom, double priceTo, int page) {
        Sort descendingSort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page, PAGE_COUNT, descendingSort);
        double startPrice = Math.min(priceFrom, priceTo);
        double endPrice = Math.max(priceFrom, priceTo);
        // Perform the repository queries with pagination
        Page<ProductItem> products = productItemRepository.findByMajorCategoryIdAndCategoryIdContainingIgnoreCaseAndSubCategoryIdContainingIgnoreCaseAndProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndSellingPriceBetweenOrMrpPriceBetween(
                majorCategory, categoryId, subCategoryId, keyword, keyword, startPrice, endPrice, startPrice, endPrice, pageable
        );

        return new ProductList(products.getContent(), page, products.getTotalElements(), globalCommonService.getTotalPage(products.getTotalPages()));
    }

    public ProductList findProductByIdsIn(String[] ids, int page) {
        Page<ProductItem> products = productItemRepository.findAllByIdIn(ids, PageRequest.of(page, PAGE_COUNT));
        // Get the content of the pages as filtered products
        List<ProductItem> filteredProducts = new ArrayList<>(products.getContent());

        return new ProductList(filteredProducts, page, products.getTotalElements(), globalCommonService.getTotalPage(products.getTotalPages()));
    }

    public List<ProductItem> findProductByIdsIn(String[] ids) {
        return productItemRepository.findAllByIdIn(ids).orElse(new ArrayList<>());
    }

    @Override
    public void preserveOrder(String id, int qty, String variant) {
        ProductItem productItem = productItemRepository.findOneById(id).orElseThrow(() -> new RuntimeException("Invalid Product Id"));
        int variantIndex = globalCommonService.findIndex(productItem.getProductVariation(), variant);
        Integer[] stocks = productItem.getStock();
        int stock = productItem.getStock()[variantIndex];
        stocks[variantIndex] = stock - qty;
        productItem.setStock(stocks);
        productItemRepository.save(productItem);
    }

    @Override
    public void unpreserve(String id, int qty, String variant) {
        ProductItem productItem = productItemRepository.findOneById(id).orElseThrow(() -> new RuntimeException("Invalid Product Id"));
        int variantIndex = globalCommonService.findIndex(productItem.getProductVariation(), variant);
        Integer[] stocks = productItem.getStock();
        int stock = productItem.getStock()[variantIndex];
        stocks[variantIndex] = stock + qty;
        productItem.setStock(stocks);
        productItemRepository.save(productItem);
    }

    private ProductCategory getCategoryId(String nameOrId) {
        Optional<ProductCategory> productCategory = productCategoryService.findOneCategoryByNameOrId(nameOrId);

        if (productCategory.isPresent()) return productCategory.get();

        ProductCategory newProductCategory = new ProductCategory();
        newProductCategory.setName(nameOrId);
        newProductCategory = productCategoryService.saveCategory(newProductCategory);
        return newProductCategory;
    }

    private ProductSubCategory getSubCategoryId(String categoryId, String nameOrId) {
        Optional<ProductSubCategory> productSubCategories = productCategoryService.getOneByCategoryIdAndName(categoryId, nameOrId);
        if (!productSubCategories.isPresent()) {
            productSubCategories = productCategoryService.getOneByCategoryIdAndId(categoryId, nameOrId);
        }

        if (productSubCategories.isPresent()) return productSubCategories.get();

        ProductSubCategory subCategory = new ProductSubCategory();
        subCategory.setName(nameOrId);
        subCategory.setCategoryId(categoryId);
        return productCategoryService.saveSubCategories(subCategory);
    }

    private String uploadFile(MultipartFile file, String id) {
        if(file == null) return "";
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

    public static MultipartFile convertBytesToMux(byte[] data, String filename, String contentType) {
        return new MockMultipartFile(filename, filename, contentType, data);
    }

    @Async
    private void deleteFile(String fileName) {
        service.deleteFile(fileName);
    }
}
