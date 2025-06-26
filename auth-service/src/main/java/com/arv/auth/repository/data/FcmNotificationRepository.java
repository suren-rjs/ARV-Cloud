package com.arv.auth.repository.data;

import com.arv.auth.model.document.Notification;
import com.google.firebase.messaging.FirebaseMessagingException;

public interface FcmNotificationRepository {
    void subscribe(String token) throws FirebaseMessagingException;
    void sendNotification(String title, String message) throws FirebaseMessagingException;
    void sendNotificationByUser(Notification notification) throws FirebaseMessagingException;
}
