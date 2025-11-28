package com.shopease.payment.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEvent {
    private Long paymentId;
    private Long orderId;
    private String status;
    private Double amount;
    private String paymentMode;
    private Instant paymentDate;
    private String referenceNumber;
}