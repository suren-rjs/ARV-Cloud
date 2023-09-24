package com.arv.auth.model.dto.request;

import com.arv.auth.model.enums.HomeBannerSection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeBannerDTO {
    private String id;
    private String name;
    private Double percentage;
    private String majorCategory;
    private String categoryId;
    private String subCategoryId;
    private byte[] uInt8List;
    private HomeBannerSection homeBannerSection = HomeBannerSection.SECTION_1;
}
