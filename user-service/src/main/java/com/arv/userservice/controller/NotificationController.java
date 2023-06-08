package com.arv.userservice.controller;

import com.arv.userservice.helper.GlobalCommonService;
import com.arv.userservice.model.document.FcmToken;
import com.arv.userservice.model.dto.request.notification.NotificationMessage;
import com.arv.userservice.repository.crud.FcmTokenRepository;
import com.arv.userservice.service.FirebaseMessagingService;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings({"unused", "rawtypes"})
@Controller
@RequestMapping(value = "/notification", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationController {

    @Autowired
    private GlobalCommonService globalCommonService;
    @Autowired
    private FirebaseMessagingService firebaseService;
    @Autowired
    private FcmTokenRepository fcmTokenRepository;


    @PostMapping
    @ResponseBody
    public ResponseEntity<?> sendNotification(@RequestBody NotificationMessage notificationMessage,
                                              @RequestParam String topic) throws FirebaseMessagingException {
        String message = firebaseService.sendNotification(notificationMessage, topic);
        return globalCommonService.getResponseEntityByMessageAndStatus(message, HttpStatus.OK);
    }

    @PostMapping("/fcm")
    @ResponseBody
    public ResponseEntity<?> addFcmToken(@RequestBody FcmToken token) {
        try {
            String uid = globalCommonService.getCurrentUser().getUsername();
            token.setUserId(uid);
            fcmTokenRepository.save(token);
            return globalCommonService.getResponseEntityByMessageAndStatus("Token Updated", HttpStatus.CREATED);
        } catch (Exception e) {
            return globalCommonService.getResponseEntityByMessageAndStatus("User not found", HttpStatus.NOT_FOUND);
        }
    }
}