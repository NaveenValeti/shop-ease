package com.shopease.customer.service;

import com.shopease.customer.dto.CustomerRequest;
import com.shopease.customer.dto.CustomerResponse;
import com.shopease.customer.model.Customer;
import com.shopease.customer.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        log.info("Creating a new customer with email: {}", customerRequest.getEmail());
        Customer customer = Customer.builder()
                .firstName(customerRequest.getFirstName())
                .lastName(customerRequest.getLastName())
                .email(customerRequest.getEmail())
                .address(customerRequest.getAddress())
                .build();
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully with ID: {}", savedCustomer.getId());
        return mapToCustomerResponse(savedCustomer);
    }

    public CustomerResponse getCustomerById(Long id) {
        log.info("Fetching customer by ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", id);
                    return new RuntimeException("Customer not found with id: " + id);
                });
        log.info("Found customer with ID: {}", id);
        return mapToCustomerResponse(customer);
    }

    public List<CustomerResponse> getAllCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAll().stream()
                .map(this::mapToCustomerResponse)
                .collect(Collectors.toList());
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest customerRequest) {
        log.info("Updating customer with ID: {}", id);
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", id);
                    return new RuntimeException("Customer not found with id: " + id);
                });
        existingCustomer.setFirstName(customerRequest.getFirstName());
        existingCustomer.setLastName(customerRequest.getLastName());
        existingCustomer.setEmail(customerRequest.getEmail());
        existingCustomer.setAddress(customerRequest.getAddress());
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        log.info("Customer updated successfully with ID: {}", updatedCustomer.getId());
        return mapToCustomerResponse(updatedCustomer);
    }

    public void deleteCustomer(Long id) {
        log.info("Deleting customer with ID: {}", id);
        customerRepository.deleteById(id);
        log.info("Customer deleted successfully with ID: {}", id);
    }

    private CustomerResponse mapToCustomerResponse(Customer customer) {
        return CustomerResponse.builder().id(customer.getId()).firstName(customer.getFirstName()).lastName(customer.getLastName()).email(customer.getEmail()).address(customer.getAddress()).build();
    }
}
