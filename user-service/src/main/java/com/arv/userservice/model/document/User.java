package com.arv.userservice.model.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("user")
public class User {
    @Id
    private String id;
    @NotNull
    @JsonIgnore
    private String username;
    @NotNull
    private String phone;
    @NotNull
    @JsonIgnore
    private String password;
    private String email;
    @NotNull
    private String profileName;
    private String profileImage;
    @NotNull
    @JsonIgnore
    private List<String> roles;
    @JsonIgnore
    private Boolean activeStatus;
}
