package com.arv.auth.repository.crud;

import com.arv.auth.model.document.UserSecret;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IUserSecret extends MongoRepository<UserSecret, String> {
    Optional<UserSecret> findOneByUsername(String username);
}
