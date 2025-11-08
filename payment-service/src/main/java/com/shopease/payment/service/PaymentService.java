package com.shopease.payment.service;

import com.shopease.payment.dto.PaymentRequest;
import com.shopease.payment.dto.PaymentResponse;
import com.shopease.payment.exception.InsufficientFundsException;
import com.shopease.payment.exception.PaymentProcessingException;
import com.shopease.payment.model.Payment;
import com.shopease.payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        log.info("Processing payment for order: {} with amount: {}", 
                paymentRequest.getOrderId(), paymentRequest.getAmount());
        
        try {
            validatePaymentRequest(paymentRequest);
            
            String referenceNumber = UUID.randomUUID().toString();
            String status = simulatePaymentProcessing(paymentRequest);
            
            Payment payment = Payment.builder()
                    .orderId(paymentRequest.getOrderId())
                    .amount(paymentRequest.getAmount())
                    .mode(paymentRequest.getPaymentMode())
                    .paymentDate(Instant.now())
                    .status(status)
                    .referenceNumber(referenceNumber)
                    .build();

            Payment savedPayment = paymentRepository.save(payment);
            log.info("Payment processed successfully. Payment ID: {}, Reference: {}", 
                    savedPayment.getId(), referenceNumber);

            return PaymentResponse.builder()
                    .paymentId(savedPayment.getId())
                    .orderId(savedPayment.getOrderId())
                    .status(savedPayment.getStatus())
                    .amount(savedPayment.getAmount())
                    .paymentDate(savedPayment.getPaymentDate())
                    .build();
                    
        } catch (Exception e) {
            log.error("Payment processing failed for order: {}", paymentRequest.getOrderId(), e);
            throw new PaymentProcessingException("Failed to process payment: " + e.getMessage(), e);
        }
    }
    
    private void validatePaymentRequest(PaymentRequest request) {
        if (request.getOrderId() <= 0) {
            throw new IllegalArgumentException("Order ID must be greater than zero");
        }
        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
        if (request.getPaymentMode() == null || request.getPaymentMode().trim().isEmpty()) {
            throw new IllegalArgumentException("Payment mode cannot be empty");
        }
        log.debug("Payment request validation passed for order: {}", request.getOrderId());
    }
    
    private String simulatePaymentProcessing(PaymentRequest request) {
        if (request.getAmount() > 10000.0) {
            log.warn("Payment amount exceeds limit for order: {}", request.getOrderId());
            throw new InsufficientFundsException("Payment amount exceeds available limit");
        }
        
        if (Math.random() < 0.05) {
            throw new PaymentProcessingException("Payment gateway temporarily unavailable");
        }
        
        return "SUCCESS";
    }
    
    public PaymentResponse getPaymentById(Long paymentId) {
        log.info("Retrieving payment with ID: {}", paymentId);
        
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> {
                    log.warn("Payment not found with ID: {}", paymentId);
                    return new PaymentProcessingException("Payment not found with ID: " + paymentId);
                });
                
        log.debug("Payment retrieved successfully: {}", payment.getId());
        
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .status(payment.getStatus())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}