package com.arv.auth.service;

import com.arv.auth.model.document.DeviceToken;
import com.arv.auth.repository.crud.DeviceTokenRepository;
import com.arv.auth.repository.data.FcmNotificationRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class FCMService implements FcmNotificationRepository {
    @Autowired
    private FirebaseMessaging fcm;
    @Autowired
    protected DeviceTokenRepository deviceTokenRepository;

    @Override
    public void subscribe(String token) throws FirebaseMessagingException {
        fcm.subscribeToTopic(Collections.singletonList(token), "arv-notification");
    }

    @Override
    public void sendNotification(String title, String message) {
        List<DeviceToken> tokens = deviceTokenRepository.findAll();
        tokens.forEach(deviceToken -> {
            Message msg = Message.builder()
                    .putData("title", title)
                    .putData("body", message)
                    .setToken(deviceToken.getDeviceToken())
                    .build();
            try {
                fcm.send(msg);
            } catch (FirebaseMessagingException ignored) {
            }
        });
    }
}