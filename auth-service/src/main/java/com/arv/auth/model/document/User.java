package com.arv.auth.model.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("user")
public class User {
    @Id
    private String id;
    @NonNull
    @JsonIgnore
    @Indexed
    private String username;
    @NonNull
    @Indexed
    private String phone;
    @NonNull
    @JsonIgnore
    private String password;
    @Indexed
    private String email;
    @NonNull
    @Indexed
    private String profileName;
    private String profileImage;
    @NonNull
    @JsonIgnore
    @Indexed
    private List<String> roles;
    @JsonIgnore
    private List<String> recentViews;
    @JsonIgnore
    private List<String> recentWishLists;
    @JsonIgnore
    private Boolean activeStatus;
    @JsonIgnore
    private Date createdDate;
    @JsonIgnore
    private Boolean isFirstOrder;
}
