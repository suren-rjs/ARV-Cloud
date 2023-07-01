package com.arv.auth.model.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("wishlist")
public class Wishlist {
    @Id
    private String id;
    @NonNull
    @JsonIgnore
    private String userId;
    @NonNull
    private String productId;
}
