package com.arv.auth.service;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.HomeBanner;
import com.arv.auth.model.dto.request.HomeBannerDTO;
import com.arv.auth.model.enums.HomeBannerSection;
import com.arv.auth.repository.crud.HomeBannerRepository;
import com.arv.auth.repository.data.HomeBannerDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
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
    public Page<HomeBanner> searchByNameAndBanner(HomeBannerSection section, String name, Pageable pageable) {
        return homeBannerRepository.findByHomeBannerSectionAndNameContainingIgnoreCase(section, name, pageable);
    }

    @Override
    public Page<HomeBanner> searchByNameContainingIgnoreCase(String name, Pageable pageable) {
        return homeBannerRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public HomeBanner findById(String id) {
        return homeBannerRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid id"));
    }

    @Override
    public HomeBanner save(HomeBannerDTO homeBannerDTO) {
        HomeBanner homeBanner = new HomeBanner();
        homeBanner.setPercentage(homeBannerDTO.getPercentage());
        homeBanner.setName(homeBannerDTO.getName());
        homeBanner.setHomeBannerSection(homeBannerDTO.getHomeBannerSection());
        homeBanner = homeBannerRepository.save(homeBanner);
        homeBanner.setImageUri(uploadFile(convertBytesToMux(homeBannerDTO.getUInt8List(), "banner-" + homeBanner.getId() + ".jpeg", "image/jpeg"), "banner-" + homeBanner.getId()));

        return homeBannerRepository.save(homeBanner);
    }

    @Override
    public void delete(String id) {
        deleteFile(id);
        homeBannerRepository.deleteById(id);
    }

    @Override
    public HomeBanner update(HomeBannerDTO homeBannerDTO) {
        HomeBanner homeBanner = homeBannerRepository.findById(homeBannerDTO.getId()).orElseThrow(() -> new RuntimeException("Invalid Id"));
        if (homeBannerDTO.getUInt8List() != null) {
            homeBanner.setImageUri(uploadFile(convertBytesToMux(homeBannerDTO.getUInt8List(), "banner-" + homeBanner.getId() + ".jpeg", "image/jpeg"), "banner-" + homeBanner.getId()));
        }
        homeBanner.setName(homeBannerDTO.getName());
        homeBanner.setPercentage(homeBannerDTO.getPercentage());
        return homeBannerRepository.save(homeBanner);
    }

    @Async
    private String uploadFile(MultipartFile file, String id) {
        return service.uploadFile(file, id);
    }

    private void deleteFile(String fileName) {
        try {
            service.deleteFile(fileName);
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
        }
    }

    public static MultipartFile convertBytesToMux(byte[] data, String filename, String contentType) {
        return new MockMultipartFile(filename, filename, contentType, data);
    }
}
