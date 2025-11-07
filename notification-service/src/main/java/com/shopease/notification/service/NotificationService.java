package com.shopease.notification.service;

import com.shopease.notification.dto.NotificationRequest;
import com.shopease.notification.dto.NotificationResponse;
import com.shopease.notification.model.Notification;
import com.shopease.notification.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationResponse sendNotification(NotificationRequest request) {
        log.info("Attempting to send {} notification to {} for entity ID: {}", request.getType(), request.getRecipient(), request.getRelatedEntityId());

        // Simulate sending notification (e.g., call an external email/SMS provider)
        // In a real application, this would involve integration with third-party services
        String status = "SENT"; // Assume success for now
        if (Math.random() < 0.1) { // 10% chance of failure for demonstration
            status = "FAILED";
            log.error("Failed to send {} notification to {}", request.getType(), request.getRecipient());
        }

        Notification notification = Notification.builder()
                .recipient(request.getRecipient())
                .type(request.getType())
                .subject(request.getSubject())
                .message(request.getMessage())
                .status(status)
                .sentDate(Instant.now())
                .relatedEntityId(request.getRelatedEntityId())
                .build();
        notificationRepository.save(notification);

        log.info("Notification sent successfully with status: {}", status);

        return NotificationResponse.builder()
                .notificationId(notification.getId())
                .status(notification.getStatus())
                .sentDate(notification.getSentDate())
                .build();
    }
}