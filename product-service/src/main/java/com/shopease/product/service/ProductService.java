package com.shopease.product.service;

import com.shopease.product.dto.ProductRequest;
import com.shopease.product.dto.ProductResponse;
import com.shopease.product.model.Product;
import com.shopease.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public ProductResponse saveProduct(ProductRequest productRequest) {
        log.info("Creating a new product with name: {}", productRequest.getName());
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .build();
        Product savedProduct = productRepository.save(product);
        log.info("Product created successfully with ID: {}", savedProduct.getId());
        return mapToResponse(savedProduct);
    }

    @Cacheable(value = "products")
    public List<ProductResponse> getAllProducts(){
        log.info("Fetching all products");
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "product", key = "#id")
    public ProductResponse getProductById(Long id){
        log.info("Fetching product by ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", id);
                    return new RuntimeException("Product not found with id: "+id);
                });
        log.info("Found product with ID: {}", id);
        return mapToResponse(product);
    }

    @CacheEvict(value = {"product", "products"}, key = "#id")
    public void deleteProduct(Long id){
        log.info("Deleting product with ID: {}", id);
        productRepository.deleteById(id);
        log.info("Product deleted successfully with ID: {}", id);
    }

    ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }
}
