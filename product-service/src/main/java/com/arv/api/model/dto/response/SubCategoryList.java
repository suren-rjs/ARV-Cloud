package com.arv.api.model.dto.response;

import com.arv.api.model.document.ProductSubCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryList {
    List<ProductSubCategory> subCategoryList;
    int currentPage;
    long totalCount;
    int totalPages;
}
