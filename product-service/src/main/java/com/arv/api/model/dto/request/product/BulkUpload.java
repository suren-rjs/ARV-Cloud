package com.arv.api.model.dto.request.product;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class BulkUpload {
    @NotNull
    private String storeId;
    @NotNull
    private MultipartFile xmlFile;
}
