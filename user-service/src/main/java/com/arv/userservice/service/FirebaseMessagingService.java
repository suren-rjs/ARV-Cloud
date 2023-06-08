package com.arv.userservice.service;

import com.arv.userservice.model.dto.request.notification.NotificationMessage;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class FirebaseMessagingService {

    @Autowired
    private FirebaseMessaging firebaseMessaging;


    public String sendNotification(NotificationMessage notificationMessage, String topic) throws FirebaseMessagingException {

        Notification notification = Notification
                .builder()
                .setTitle(notificationMessage.getSubject())
                .setBody(notificationMessage.getContent())
                .setImage(notificationMessage.getImage())
                .build();

        Message message = Message
                .builder()
                .setTopic(topic)
                .setNotification(notification)
                .putAllData(notificationMessage.getData())
                .build();

        return firebaseMessaging.send(message);
    }

}