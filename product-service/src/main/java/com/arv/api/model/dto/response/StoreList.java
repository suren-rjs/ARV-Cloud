package com.arv.api.model.dto.response;

import com.arv.api.model.document.StoreInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreList {
    List<StoreInfo> stores;
    int currentPage;
    long totalCount;
    int totalPages;
}
