package com.arv.userservice.model.dto.request.auth;

import com.arv.userservice.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUp {
    private String phone;
    private String username;
    private String uid;
    private UserType userType;
    private String email;
}
