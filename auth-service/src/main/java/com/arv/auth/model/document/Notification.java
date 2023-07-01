package com.arv.auth.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("notification")
public class Notification {
    @Id
    private String id;
    @NotNull
    String title;
    @NotNull
    String message;
    @NotNull
    String userId;
}
