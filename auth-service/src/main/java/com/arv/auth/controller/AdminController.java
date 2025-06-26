package com.arv.auth.controller;

import com.arv.auth.helper.GlobalCommonService;
import com.arv.auth.model.document.Notification;
import com.arv.auth.model.document.User;
import com.arv.auth.model.rest.UsersList;
import com.arv.auth.repository.crud.DeviceTokenRepository;
import com.arv.auth.repository.crud.UserRepository;
import com.arv.auth.repository.data.FcmNotificationRepository;
import com.arv.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@SuppressWarnings("All")
@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {
    @Value("${product.pageSize}")
    private int PAGE_COUNT;

    @Autowired
    private GlobalCommonService globalCommonService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    protected FcmNotificationRepository fcm;

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    @Autowired
    private UserService userService;

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
    public ResponseEntity<?> getAllUsers(@RequestParam String keyword, @RequestParam(defaultValue = "0") Integer page) {
        Sort descendingSort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page, PAGE_COUNT, descendingSort);
        if (!keyword.replaceAll(" ", "").isEmpty()) {
            return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
        }
        Page<User> usersPage = userRepository.findAllByUsernameContainingIgnoreCaseOrProfileNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, keyword, pageable);


        UsersList usersList = new UsersList(
                usersPage.getContent(),
                page,
                globalCommonService.getTotalPage(usersPage.getTotalPages()),
                usersPage.getNumberOfElements()
        );
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    @PutMapping("/changeActiveStatus/{id}")
    public ResponseEntity<?> disableUser(@PathVariable("id") String id) {
        String responseMessage;
        HttpStatus responseStatus = HttpStatus.OK;

        responseMessage = userService.changeActiveStatusById(id);
        return globalCommonService.getResponseEntityByMessageAndStatus(responseMessage, responseStatus);
    }

    @PostMapping("/notification")
    public ResponseEntity<?> sendNotification(@RequestBody Notification notification) {
        String responseMessage = "Sending notification... ";
        HttpStatus responseStatus = HttpStatus.OK;
        try {
            fcm.sendNotification(notification.getTitle(), notification.getMessage());
        } catch (Exception e) {
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(responseMessage, responseStatus);
    }

    @PostMapping("/notify-user")
    public ResponseEntity<?> motifyUser(@RequestBody Notification notification) {
        String responseMessage = "Sending notification!";
        HttpStatus responseStatus = HttpStatus.OK;
        try {
            fcm.sendNotificationByUser(notification);
        } catch (Exception e) {
            responseMessage = "Subscription failed";
        }
        return globalCommonService.getResponseEntityByMessageAndStatus(responseMessage, responseStatus);
    }
}
