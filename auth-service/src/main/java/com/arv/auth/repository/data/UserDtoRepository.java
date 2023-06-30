package com.arv.auth.repository.data;


import com.arv.auth.model.dto.request.auth.SignUp;
import com.arv.auth.model.document.User;

import java.util.List;

@SuppressWarnings("unused")
public interface UserDtoRepository {
    void save(SignUp user);

    List<User> findAll();

    User findOneByUsername(String username);

    String changeActiveStatusById(String id);
}
