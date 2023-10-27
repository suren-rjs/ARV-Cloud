package com.arv.auth.service;

import com.arv.auth.model.document.User;
import com.arv.auth.model.document.UserSecret;
import com.arv.auth.model.dto.request.auth.SignUp;
import com.arv.auth.repository.crud.IUserSecret;
import com.arv.auth.repository.data.UserDtoRepository;
import com.arv.auth.repository.crud.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    protected IUserSecret userSecret;

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
        CustomByCrypt customByCrypt = CustomByCrypt.getInstance();
        User user = new User();
        user.setUsername(signUp.getPhone());
        user.setPhone(signUp.getPhone());
        user.setEmail(signUp.getEmail());
        user.setProfileName(signUp.getUsername());
        String secret = customByCrypt.generateSaltPhrase();
        user.setPhrase(customByCrypt.encrypt(signUp.getUid(), secret));
        userSecret.save(new UserSecret(null, signUp.getPhone(), secret));
        user.setPassword(bCryptPasswordEncoder.encode(signUp.getUid()));
        user.setActiveStatus(true);
        user.setIsFirstOrder(true);

        List<String> roles = new ArrayList<>();

        switch (signUp.getUserType()) {
            case ADMIN:
                roles = Arrays.asList("ADMIN", "MANAGER", "CUSTOMER");
                break;
            case MANAGER:
                roles = Arrays.asList("MANAGER, CUSTOMER");
                break;
            default:
                roles = Arrays.asList("CUSTOMER");
                break;
        }

        user.setRoles(roles);
        user.setRecentViews(new ArrayList<>());
        user.setRecentWishLists(new ArrayList<>());
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

