package com.shopease.order.service;

import com.shopease.order.client.PaymentClient;
import com.shopease.order.dto.OrderRequest;
import com.shopease.order.dto.OrderResponse;
import com.shopease.order.dto.PaymentRequest;
import com.shopease.order.dto.ProductDto;
import com.shopease.order.model.Order;
import com.shopease.order.repository.OrderRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;

    public OrderService(OrderRepository repository, ProductClient productClient, PaymentClient paymentClient) {
        this.repository = repository;
        this.productClient = productClient;
        this.paymentClient = paymentClient;
    }

    public OrderResponse createOrder(OrderRequest request) {
        ProductDto product;
        try {
            product = productClient.getProductById(request.getProductId());
        } catch (FeignException.NotFound e) {
            throw new RuntimeException("Product not found with id: " + request.getProductId() + ". Please ensure the product exists before creating an order.", e);
        }

        if (product.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock for product id: " + request.getProductId() + ". Available quantity: " + product.getQuantity() + ", Requested: " + request.getQuantity());
        }

        double totalPrice = product.getPrice() * request.getQuantity();

        Order order = new Order();
        order.setProductId(product.getId());
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(totalPrice);

        repository.save(order);

        // Initiate payment
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .amount(totalPrice)
                .paymentMode("CARD") // Example payment mode
                .build();
        paymentClient.doPayment(paymentRequest);

        return new OrderResponse(order.getId(), product.getName(), order.getQuantity(), order.getTotalPrice());
    }

    public List<OrderResponse> getAllOrders() {
        return repository.findAll().stream().map(order -> {
            ProductDto product = productClient.getProductById(order.getProductId());
            return new OrderResponse(order.getId(), product.getName(), order.getQuantity(), order.getTotalPrice());
        }).collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
        ProductDto product = productClient.getProductById(order.getProductId());
        return new OrderResponse(order.getId(), product.getName(), order.getQuantity(), order.getTotalPrice());
    }

    public void deleteOrder(Long id) {
        repository.deleteById(id);
    }
}
