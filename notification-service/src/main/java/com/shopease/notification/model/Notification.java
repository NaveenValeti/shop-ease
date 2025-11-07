package com.shopease.notification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipient;
    private String type; // EMAIL, SMS, PUSH
    private String subject;
    private String message;
    private String status; // SENT, FAILED, PENDING
    private Instant sentDate;
    private Long relatedEntityId; // e.g., orderId, customerId
}