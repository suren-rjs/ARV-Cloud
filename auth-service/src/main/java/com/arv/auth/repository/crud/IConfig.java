package com.arv.auth.repository.crud;

import com.arv.auth.model.document.Config;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IConfig extends MongoRepository<Config, String> {
    Optional<Config> findByKeyContainingIgnoreCase(String key);
}
