package com.arv.auth.model.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("user")
public class User {
    @Id
    private String id;
    @NonNull
    @JsonIgnore
    private String username;
    @NonNull
    private String phone;
    @NonNull
    @JsonIgnore
    private String password;
    private String email;
    @NonNull
    private String profileName;
    private String profileImage;
    @NonNull
    @JsonIgnore
    private List<String> roles;
    @JsonIgnore
    private Boolean activeStatus;
}
