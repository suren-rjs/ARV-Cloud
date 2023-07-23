package com.arv.auth.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeBannerDTO {
    private String id;
    private String name;
    private double percentage;
    private byte[] uInt8List;
}
