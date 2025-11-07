package com.shopease.order.client;

import com.shopease.order.dto.PaymentRequest;
import com.shopease.order.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentClient {
    @PostMapping("/api/payments")
    PaymentResponse doPayment(@RequestBody PaymentRequest paymentRequest);
}