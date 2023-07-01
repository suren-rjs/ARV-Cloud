package com.arv.api.model.dto.response;

import com.arv.api.model.document.ProductItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductList {
    List<ProductItem> products;
    int currentPage;
    long totalCount;
    int totalPages;
}
