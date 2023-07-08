package com.arv.api.model.dto.response;

import com.arv.api.model.document.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryList {
    List<ProductCategory> categoryList;
    int currentPage;
    long totalCount;
    int totalPages;
}
