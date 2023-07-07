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
@Document("cart")
public class Cart {
    @Id
    private String id;
    @NonNull
    @JsonIgnore
    @Indexed
    private String userId;
    @NonNull
    @Indexed
    private String productId;
}
