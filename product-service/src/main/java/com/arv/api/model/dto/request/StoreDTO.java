package com.arv.api.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreDTO {
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String latitude;
    @NotNull
    private String longitude;
    private String locationLink;
    private Double minDeliveryPrice;
    private String tags;
}
