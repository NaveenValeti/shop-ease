package com.shopease.order.consumer;

import com.shopease.order.event.PaymentEvent;
import com.shopease.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentEventConsumer {

    private final OrderService orderService;

    public PaymentEventConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "payment-events", groupId = "order-service-group")
    public void handlePaymentEvent(PaymentEvent paymentEvent) {
        log.info("Received payment event for order: {} with status: {}", 
                paymentEvent.getOrderId(), paymentEvent.getStatus());
        
        try {
            orderService.updateOrderPaymentStatus(paymentEvent.getOrderId(), paymentEvent.getStatus());
            log.info("Successfully updated order payment status for order: {}", paymentEvent.getOrderId());
        } catch (Exception e) {
            log.error("Failed to update order payment status for order: {}", paymentEvent.getOrderId(), e);
        }
    }
}