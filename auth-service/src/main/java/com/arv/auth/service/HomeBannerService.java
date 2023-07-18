package com.arv.auth.service;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.HomeBanner;
import com.arv.auth.model.dto.request.HomeBannerDTO;
import com.arv.auth.repository.crud.HomeBannerRepository;
import com.arv.auth.repository.data.HomeBannerDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HomeBannerService implements HomeBannerDtoRepository {
    @Autowired
    protected GlobalCommonService globalCommonService;
    @Autowired
    protected HomeBannerRepository homeBannerRepository;
    @Autowired
    protected StorageService service;

    @Override
    public Page<HomeBanner> findAll(Pageable pageable) {
        return homeBannerRepository.findAll(pageable);
    }

    @Override
    public HomeBanner findById(String id) {
        return homeBannerRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid id"));
    }

    @Override
    public HomeBanner save(HomeBannerDTO homeBannerDTO) {
        HomeBanner homeBanner = new HomeBanner();
        homeBanner.setPercentage(homeBannerDTO.getPercentage());
        homeBanner = homeBannerRepository.save(homeBanner);
        homeBanner.setImageUri(uploadFile(homeBannerDTO.getImage(), homeBanner.getId()));

        return homeBannerRepository.save(homeBanner);
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public HomeBanner update(HomeBannerDTO homeBannerDTO) {
        HomeBanner homeBanner = homeBannerRepository.findById(homeBannerDTO.getId()).orElseThrow(() -> new RuntimeException("Invalid Id"));
        if (homeBannerDTO.getImage() != null) {
            deleteFile(homeBanner.getId());
            homeBanner.setImageUri(uploadFile(homeBannerDTO.getImage(), homeBanner.getId()));
        }
        homeBanner.setPercentage(homeBannerDTO.getPercentage());
        return homeBannerRepository.save(homeBanner);
    }

    @Async
    private String uploadFile(MultipartFile file, String id) {
        return service.uploadFile(file, id);
    }

    private void deleteFile(String fileName) {
        service.deleteFile(fileName);
    }
}
