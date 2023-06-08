package com.arv.userservice.service;

import com.arv.userservice.model.document.User;
import com.arv.userservice.model.dto.request.auth.SignUp;
import com.arv.userservice.repository.crud.UserRepository;
import com.arv.userservice.repository.data.UserDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"All"})
@Service
public class UserService implements UserDtoRepository, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Lazy
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findOneByUsername(username);
        UserDetailsImpl userDetails;
        if (user.isPresent()) {
            userDetails = new UserDetailsImpl();
            userDetails.setUser(user.get());
        } else {
            throw new UsernameNotFoundException("User not exists with username: " + username);
        }
        return userDetails;
    }

    @Override
    public void save(SignUp signUp) {
        User user = new User();
        user.setUsername(signUp.getPhone());
        user.setPhone(signUp.getPhone());
        user.setEmail(signUp.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(signUp.getUid()));
        user.setActiveStatus(true);

        List<String> roles = new ArrayList<>();

        switch (signUp.getUserType()) {
            case ADMIN:
                roles = List.of("ADMIN", "MANAGER", "CUSTOMER");
                break;
            case MANAGER:
                roles = List.of("MANAGER, CUSTOMER");
                break;
            default:
                roles = List.of("CUSTOMER");
                break;
        }

        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findOneByUsername(String username) {
        return userRepository.findOneByUsername(username).orElse(null);
    }

    @Override
    public String changeActiveStatusById(String id) {
        String message = "Invalid userId";
        User userById = userRepository.findOneById(id).orElse(null);
        if (userById != null) {
            userById.setActiveStatus(!userById.getActiveStatus());
            message = "User status hasbeen " + (userById.getActiveStatus() ? "Enabled" : "Disabled");
            userRepository.save(userById);
        }
        return message;
    }
}

