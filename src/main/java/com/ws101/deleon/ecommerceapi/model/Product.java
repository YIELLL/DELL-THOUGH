package com.ws101.deleon.ecommerceapi.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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

    // Constructors
    public Product() {
    }

    public Product(Long id, String name, String description, Double price, String category, Integer stockQuantity, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}