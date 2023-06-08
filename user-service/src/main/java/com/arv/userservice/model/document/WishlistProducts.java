package com.arv.userservice.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("wishlist")
public class WishlistProducts {
    @Id
    private String id;

    @NotNull
    private String customerId;

    @NotNull
    private String productId;
}
