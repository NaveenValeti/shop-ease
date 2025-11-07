package com.shopease.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

//This represents the incoming data from the client.
public class ProductRequest {
    private String name;
    private String description;
    private double price;
    private int quantity;
}
