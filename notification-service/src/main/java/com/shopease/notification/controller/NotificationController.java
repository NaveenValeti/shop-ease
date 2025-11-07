package com.shopease.notification.controller;

import com.shopease.notification.dto.NotificationRequest;
import com.shopease.notification.dto.NotificationResponse;
import com.shopease.notification.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<NotificationResponse> sendNotification(@RequestBody NotificationRequest notificationRequest) {
        NotificationResponse response = notificationService.sendNotification(notificationRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Potentially add endpoints for getting notification history, status, etc.
}