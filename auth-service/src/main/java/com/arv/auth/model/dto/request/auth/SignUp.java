package com.arv.auth.model.dto.request.auth;

import com.arv.auth.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUp {
    private String phone;
    private String username = "";
    private String uid;
    private UserType userType = UserType.CUSTOMER;
    private String email;
}
