package com.arv.api.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document("store-info")
public class StoreInfo {
    @Id
    private String id;
    @NotNull
    @UniqueElements
    private String name;
    @NotNull
    private String latitude;
    @NotNull
    private String longitude;
    private String locationLink;
    private Double minDeliveryPrice;
    private Double extensivePrice;
    private String tags;
}
