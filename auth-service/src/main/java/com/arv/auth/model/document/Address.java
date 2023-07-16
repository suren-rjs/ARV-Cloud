package com.arv.auth.model.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("address")
public class Address {
    @Id
    private String id;
    @NonNull
    @JsonIgnore
    @Indexed
    private String userId;
    @NonNull
    @Indexed
    private String name;
    @NonNull
    @Indexed
    private String phone;
    @NonNull
    private String addressLine1;
    private String addressLine2;
    @NonNull
    private String area;
    @NonNull
    @Indexed
    private String pinCode;
    @NonNull
    private String state;
    @NonNull
    private String nation;
    private String landMark;
    private Boolean isDeliveryAddress = false;
}
