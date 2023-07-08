package com.arv.api.model.dto.request.product;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class StoreDTO {
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String latitude;
    @NotNull
    private String longitude;
    private String tags;
}
