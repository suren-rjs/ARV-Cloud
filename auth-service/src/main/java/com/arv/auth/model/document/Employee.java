package com.arv.auth.model.document;

import com.arv.auth.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("employee")
public class Employee {
    @Id
    String id;
    String name;
    String phone;
    UserType type = UserType.DELIVERY_PERSON;
}
