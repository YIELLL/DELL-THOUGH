package com.ws101.deleon.ecommerceapi.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Product entity representing an item in the e-commerce catalog.
 * 
 * This class models a product with essential e-commerce attributes including
 * identification, pricing, inventory, and categorization information.
 * 
 * @author Kent Jeanne S. De Leon
 * @author Keniel Drew D. De Asis
 * @see ProductService
 * @see ProductController
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    /**
     * Unique identifier for the product.
     */
    private Long id;
    
    /**
     * Name of the product.
     * Required field with minimum length validation.
     */
    @NotBlank(message = "Product name is required")
    private String name;
    
    /**
     * Detailed description of the product.
     */
    @NotBlank(message = "Product description is required")
    private String description;
    
    /**
     * Price of the product in the catalog.
     * Must be a positive number.
     */
    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be greater than zero")
    private Double price;
    
    /**
     * Category classification for the product.
     * Required field for filtering and organization.
     */
    @NotBlank(message = "Product category is required")
    private String category;
    
    /**
     * Available stock quantity for the product.
     * Must be a non-negative integer.
     */
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;
    
    /**
     * URL to the product image (optional).
     * Can be null if no image is available.
     */
    private String imageUrl;
}