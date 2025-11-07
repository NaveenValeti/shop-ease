package com.shopease.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {
    private String recipient; // e.g., email address, phone number
    private String type;      // e.g., "EMAIL", "SMS", "PUSH"
    private String subject;   // For email
    private String message;
    private Long relatedEntityId; // e.g., orderId, customerId
}