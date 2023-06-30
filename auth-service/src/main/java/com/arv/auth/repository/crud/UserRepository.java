package com.arv.auth.repository.crud;


import com.arv.auth.model.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findOneByUsername(String username);

    List<User> findAllByUsernameContainingIgnoreCaseOrProfileNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String profileName, String email);

    Optional<User> findOneByPhone(String username);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneById(String id);
}
