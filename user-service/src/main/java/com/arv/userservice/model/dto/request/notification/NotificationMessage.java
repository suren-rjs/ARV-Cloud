package com.arv.userservice.model.dto.request.notification;

import lombok.Data;

import java.util.Map;

@Data
public class NotificationMessage {
    private String subject;
    private String content;
    private Map<String, String> data;
    private String image;
}