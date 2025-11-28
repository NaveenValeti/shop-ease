package com.shopease.order.service;

import com.shopease.order.client.PaymentClient;
import com.shopease.order.dto.OrderRequest;
import com.shopease.order.dto.OrderResponse;
import com.shopease.order.dto.PaymentRequest;
import com.shopease.order.dto.ProductDto;
import com.shopease.order.model.Order;
import com.shopease.order.repository.OrderRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository repository;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;

    public OrderService(OrderRepository repository, ProductClient productClient, PaymentClient paymentClient) {
        this.repository = repository;
        this.productClient = productClient;
        this.paymentClient = paymentClient;
    }

    public OrderResponse createOrder(OrderRequest request) {
        log.info("Creating order for product ID: {} with quantity: {}", request.getProductId(), request.getQuantity());

        ProductDto product;
        try {
            log.info("Fetching product details for product ID: {}", request.getProductId());
            product = productClient.getProductById(request.getProductId());
            log.info("Successfully fetched product details for product ID: {}", request.getProductId());
        } catch (FeignException.NotFound e) {
            log.error("Product not found with ID: {}. Order creation failed.", request.getProductId(), e);
            throw new RuntimeException("Product not found with id: " + request.getProductId() + ". Please ensure the product exists before creating an order.", e);
        }

        if (product.getQuantity() < request.getQuantity()) {
            log.error("Insufficient stock for product ID: {}. Available: {}, Requested: {}. Order creation failed.", request.getProductId(), product.getQuantity(), request.getQuantity());
            throw new RuntimeException("Insufficient stock for product id: " + request.getProductId() + ". Available quantity: " + product.getQuantity() + ", Requested: " + request.getQuantity());
        }

        double totalPrice = product.getPrice() * request.getQuantity();
        log.info("Calculated total price: {}", totalPrice);

        Order order = new Order();
        order.setProductId(product.getId());
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(totalPrice);

        repository.save(order);
        log.info("Order saved successfully with ID: {}", order.getId());

        // Initiate payment
        log.info("Initiating payment for order ID: {}", order.getId());
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .amount(totalPrice)
                .paymentMode("CARD") // Example payment mode
                .build();
        try {
            paymentClient.doPayment(paymentRequest);
            log.info("Payment request sent successfully for order ID: {}", order.getId());
        } catch (Exception e) {
            log.error("Payment service call failed for order ID: {}. Payment may need to be reconciled.", order.getId(), e);
            // Depending on business requirements, you might want to throw an exception here or handle it differently
        }

        return new OrderResponse(order.getId(), product.getName(), order.getQuantity(), order.getTotalPrice());
    }

    public List<OrderResponse> getAllOrders() {
        log.info("Fetching all orders");
        return repository.findAll().stream().map(order -> {
            ProductDto product = productClient.getProductById(order.getProductId());
            return new OrderResponse(order.getId(), product.getName(), order.getQuantity(), order.getTotalPrice());
        }).collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        log.info("Fetching order by ID: {}", id);
        Order order = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", id);

                    return new RuntimeException("Order not found: " + id);
                });
        log.info("Found order with ID: {}", id);
        ProductDto product = productClient.getProductById(order.getProductId());
        return new OrderResponse(order.getId(), product.getName(), order.getQuantity(), order.getTotalPrice());
    }

    public void deleteOrder(Long id) {
        log.info("Deleting order with ID: {}", id);
        repository.deleteById(id);
        log.info("Order deleted successfully with ID: {}", id);
    }

    public void updateOrderPaymentStatus(Long orderId, String paymentStatus) {
        log.info("Updating payment status for order: {} to status: {}", orderId, paymentStatus);
        Order order = repository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", orderId);
                    return new RuntimeException("Order not found: " + orderId);
                });
        
        // Update order status based on payment status
        if ("SUCCESS".equals(paymentStatus)) {
            order.setStatus("PAID");
            log.info("Order {} marked as PAID", orderId);
        } else if ("FAILED".equals(paymentStatus)) {
            order.setStatus("PAYMENT_FAILED");
            log.info("Order {} marked as PAYMENT_FAILED", orderId);
        }
        
        repository.save(order);
        log.info("Order payment status updated successfully for order: {}", orderId);
    }
}
