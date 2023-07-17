package com.arv.api.repository.data;

import com.arv.api.model.document.StoreInfo;
import com.arv.api.model.dto.request.StoreDTO;
import com.arv.api.model.dto.response.StoreList;

import java.util.List;

public interface StoreDtoRepository {

    void save(StoreDTO storeDTO);

    void deleteById(String id) throws Exception;

    void updateById(StoreDTO storeDTO) throws Exception;

    StoreInfo findById(String id) throws Exception;

    StoreList findAllStores(int pageNumber);

    void saveAll(List<StoreInfo> StoreInfo);

    StoreList search(String keyword, int pageNumber);
}
