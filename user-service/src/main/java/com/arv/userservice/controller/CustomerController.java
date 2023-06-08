package com.arv.userservice.controller;

import com.arv.userservice.helper.GlobalCommonService;
import com.arv.userservice.model.document.User;
import com.arv.userservice.repository.crud.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@SuppressWarnings("All")
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class CustomerController {
    @Autowired
    private GlobalCommonService globalCommonService;
    @Autowired
    private UserRepository userRepository;

    @PutMapping("")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        String responseMessage;
        HttpStatus responseStatus = HttpStatus.NOT_ACCEPTABLE;

        Optional<User> existUser = userRepository.findOneByUsername(user.getUsername());
        if (existUser.isPresent()) {
            User existUserDetails = existUser.get();
            user.setId(existUserDetails.getId());
            userRepository.save(user);
            responseStatus = HttpStatus.ACCEPTED;
            responseMessage = "User details updated !";
        } else {
            responseMessage = "Invalid userid " + user.getUsername();
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(responseMessage, responseStatus);
    }

    @GetMapping("/myProfile")
    public ResponseEntity<?> getCurrentUser() {
        return new ResponseEntity<>(globalCommonService.getCurrentUser(), HttpStatus.OK);
    }
}

