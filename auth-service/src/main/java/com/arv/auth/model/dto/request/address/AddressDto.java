package com.arv.auth.model.dto.request.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private String id;
    @NonNull
    @JsonIgnore
    private String userId;
    @NonNull
    private String name;
    @NonNull
    private String phone;
    @NonNull
    private String addressLine1;
    private String addressLine2;
    @NonNull
    private String area;
    @NonNull
    private String pinCode;
    @NonNull
    private String state;
    @NonNull
    private String nation;
    private String landMark;
}

