package com.arv.api.service;

import com.arv.api.helper.GlobalCommonService;
import com.arv.api.model.document.StoreInfo;
import com.arv.api.model.dto.request.StoreDTO;
import com.arv.api.model.dto.response.StoreList;
import com.arv.api.repository.crud.StoreRepository;
import com.arv.api.repository.data.StoreDtoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService implements StoreDtoRepository {

    @Value("${product.pageSize}")
    protected int PAGE_COUNT;
    @Autowired
    protected StoreRepository storeRepository;
    @Autowired
    protected ModelMapper modelMapper;
    @Autowired
    protected GlobalCommonService globalCommonService;

    @Override
    public void save(StoreDTO storeDTO) {
        StoreInfo store = modelMapper.map(storeDTO, StoreInfo.class);
        storeRepository.save(store);
    }

    @Override
    public void deleteById(String id) {
        if (!storeRepository.existsById(id)) {
            throw new RuntimeException("Invalid id");
        }
        storeRepository.deleteById(id);
    }

    @Override
    public void updateById(StoreDTO storeDTO) {
        if (!storeRepository.existsById(storeDTO.getId())) {
            throw new RuntimeException("Invalid id");
        }
        StoreInfo store = modelMapper.map(storeDTO, StoreInfo.class);
        storeRepository.save(store);
    }

    @Override
    public StoreInfo findById(String id) {
        if (!storeRepository.existsById(id)) {
            throw new RuntimeException("Invalid id");
        }
        return storeRepository.findById(id).orElse(null);
    }

    @Override
    public StoreList findAllStores(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_COUNT);
        Page<StoreInfo> storePage = storeRepository.findAll(pageable);
        List<StoreInfo> stores = storePage.getContent();
        return new StoreList(stores, pageNumber, storePage.getTotalElements(), globalCommonService.getTotalPage(storePage.getTotalPages()));
    }

    @Override
    public void saveAll(List<StoreInfo> Store) {

    }

    @Override
    public StoreList search(String keyword, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_COUNT);
        Page<StoreInfo> storePage = storeRepository.findByNameContainingIgnoreCase(keyword, pageable);
        List<StoreInfo> stores = storePage.getContent();
        return new StoreList(stores, pageNumber, storePage.getTotalElements(), globalCommonService.getTotalPage(storePage.getTotalPages()));
    }
}
