package com.arv.api.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryDTO {
    private String id;
    @NotNull
    private String name;
    private String majorCategory;
    private byte[] image;
}
