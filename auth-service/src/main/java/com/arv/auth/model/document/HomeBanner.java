package com.arv.auth.model.document;

import com.arv.auth.model.enums.HomeBannerSection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("banner")
public class HomeBanner {
    @Id
    private String id;
    private double percentage;
    @Indexed
    private String name;
    private String imageUri;
    @Indexed
    private HomeBannerSection homeBannerSection = HomeBannerSection.SECTION_1;
}
