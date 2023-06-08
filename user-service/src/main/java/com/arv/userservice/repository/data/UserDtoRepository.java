package com.arv.userservice.repository.data;

import com.arv.userservice.model.document.User;
import com.arv.userservice.model.dto.request.auth.SignUp;

import java.util.List;

@SuppressWarnings("unused")
public interface UserDtoRepository {
    void save(SignUp user);

    List<User> findAll();

    User findOneByUsername(String username);

    String changeActiveStatusById(String id);
}
