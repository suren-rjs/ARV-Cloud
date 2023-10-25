package com.arv.auth.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("device-token")
public class DeviceToken {
    @Id
    private String id;
    @NonNull
    String username;
    String deviceToken;
}
