package com.arv.auth.service;

import com.arv.auth.model.document.DeviceToken;
import com.arv.auth.model.document.Notification;
import com.arv.auth.repository.crud.DeviceTokenRepository;
import com.arv.auth.repository.data.FcmNotificationRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@SuppressWarnings("unused")
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
    @Async
    public void sendNotification(String title, String message) {

        Message msg = Message.builder()
                .setNotification(new com.google.firebase.messaging.Notification(title, message))
                .setTopic("arv-notification")
                .build();
        try {
            fcm.send(msg);
        } catch (FirebaseMessagingException ignored) {
        }
    }

    @Override
    public void sendNotificationByUser(Notification notification) throws RuntimeException {
        DeviceToken deviceToken = deviceTokenRepository.findOneByUsername(notification.getUsername()).orElse(null);
        if (deviceToken == null) throw new RuntimeException("Unable to send notification");
        Message msg = Message.builder()
                .setNotification(new com.google.firebase.messaging.Notification(notification.getTitle(), notification.getMessage()))
                .setToken(deviceToken.getDeviceToken())
                .build();
        try {
            fcm.send(msg);
        } catch (FirebaseMessagingException ignored) {
        }
    }
}