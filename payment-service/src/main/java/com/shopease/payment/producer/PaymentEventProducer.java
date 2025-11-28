package com.shopease.payment.producer;

import com.shopease.payment.event.PaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;
    private static final String TOPIC = "payment-events";

    public PaymentEventProducer(KafkaTemplate<String, PaymentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPaymentEvent(PaymentEvent event) {
        log.info("Publishing payment event for order: {}", event.getOrderId());
        kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event);
        log.info("Payment event published successfully for order: {}", event.getOrderId());
    }
}