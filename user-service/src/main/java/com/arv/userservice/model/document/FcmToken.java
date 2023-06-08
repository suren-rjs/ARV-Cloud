package com.arv.userservice.model.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Document("FcmTokens")
public class FcmToken {
    @Id
    private String id;

    @NonNull
    private String value;

    @NonNull
    private String userId;
}
