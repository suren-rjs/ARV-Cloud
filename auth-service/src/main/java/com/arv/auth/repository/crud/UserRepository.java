package com.arv.auth.repository.crud;


import com.arv.auth.model.document.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findOneByUsername(String username);

    Page<User> findAllByUsernameContainingIgnoreCaseOrProfileNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String profileName, String email, Pageable pageable);

    Optional<User> findOneByPhone(String username);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneById(String id);
}
