package com.arv.userservice.controller;

import com.arv.userservice.helper.GlobalCommonService;
import com.arv.userservice.model.document.User;
import com.arv.userservice.repository.crud.UserRepository;
import com.arv.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@SuppressWarnings("All")
@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {
    @Autowired
    private GlobalCommonService globalCommonService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     *
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        String message = "Invalid user id";
        HttpStatus status = HttpStatus.OK;
        ResponseEntity<?> response;
        try {
            Optional<User> user = userRepository.findOneById(id);
            if (user.isPresent()) {
                return new ResponseEntity<>(user, status);
            } else {
                status = HttpStatus.NOT_FOUND;
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            return globalCommonService.getResponseEntityByMessageAndStatus(message, status);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @PutMapping("/changeActiveStatus/{id}")
    public ResponseEntity<?> disableUser(@PathVariable("id") String id) {
        String responseMessage;
        HttpStatus responseStatus = HttpStatus.OK;

        responseMessage = userService.changeActiveStatusById(id);
        return globalCommonService.getResponseEntityByMessageAndStatus(responseMessage, responseStatus);
    }
}
