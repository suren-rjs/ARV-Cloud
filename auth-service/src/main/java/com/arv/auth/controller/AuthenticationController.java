package com.arv.auth.controller;

import com.arv.auth.config.utils.JwtTokenProvider;
import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.DeviceToken;
import com.arv.auth.model.document.User;
import com.arv.auth.model.dto.request.auth.SignUp;
import com.arv.auth.repository.crud.DeviceTokenRepository;
import com.arv.auth.repository.crud.UserRepository;
import com.arv.auth.repository.data.FcmNotificationRepository;
import com.arv.auth.service.DeviceTokenService;
import com.arv.auth.service.UserService;
import com.arv.auth.model.dto.request.auth.Login;
import com.arv.auth.model.dto.response.auth.JsonToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Timer;

@SuppressWarnings("All")
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {
    private final Timer timer = new Timer();
    @Autowired
    private GlobalCommonService globalCommonService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected DeviceTokenService deviceTokenService;

    @Autowired
    protected FcmNotificationRepository fcm;

    @PostMapping(value = "/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Login login) {
        if (login.getPhone().contains("@") && globalCommonService.validateEmail(login.getPhone()))
            return globalCommonService.getResponseEntityByMessageAndStatus("Invalid email", HttpStatus.BAD_REQUEST);

        Authentication authentication;
        final String token;
        ResponseEntity<?> tokenResponseEntity;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getPhone(), login.getUid()));
            // Todo: notification for login alert

            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenProvider.generateToken(authentication);
            tokenResponseEntity = new ResponseEntity<>(new JsonToken(token), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            tokenResponseEntity = globalCommonService.getResponseEntityByMessageAndStatus("Invalid username or password.", HttpStatus.NOT_FOUND);
        }

        return tokenResponseEntity;
    }


    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody SignUp user) {
        String responseMessage;
        HttpStatus responseStatus = HttpStatus.NOT_ACCEPTABLE;

        boolean isExistUser = userRepository.findOneByPhone(user.getPhone()).isPresent();
        if (isExistUser) {
            responseMessage = "Registered mobile number";
        } else {
            responseMessage = "New user created for " + user.getUsername();
            responseStatus = HttpStatus.CREATED;
            userService.save(user);
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(responseMessage, responseStatus);
    }

    @GetMapping("/myProfile")
    public ResponseEntity<?> getCurrentUser() {
        return new ResponseEntity<>(globalCommonService.getCurrentUser(), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        String responseMessage;
        HttpStatus responseStatus = HttpStatus.NOT_ACCEPTABLE;

        Optional<User> existUser = userRepository.findOneByUsername(user.getUsername());
        if (existUser.isPresent()) {
            User existUserDetails = existUser.get();
            existUserDetails.setProfileName(user.getProfileName());
            userRepository.save(existUserDetails);
            responseMessage = "User details updated !";
        } else {
            responseMessage = "Invalid userid " + user.getUsername();
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(responseMessage, responseStatus);
    }

    @PostMapping("/device-token")
    public ResponseEntity<?> addDeviceToken(@RequestBody DeviceToken deviceToken) {
        String responseMessage = "Subscription Completed!";
        HttpStatus responseStatus = HttpStatus.OK;
        deviceTokenService.save(deviceToken);
        try {
            fcm.subscribe(deviceToken.getDeviceToken());
        } catch (Exception e) {
            responseMessage = "Subscription failed";
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(responseMessage, responseStatus);
    }
}
