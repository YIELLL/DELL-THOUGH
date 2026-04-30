package com.ws101.deleon.ecommerceapi.model;

import jakarta.persistence.*;
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
 * Persisted to the database with One-to-Many relationship to Category.
 * 
 * @author Kent Jeanne S. De Leon
 * @author Keniel Drew D. De Asis
 * @see com.ws101.deleon.ecommerceapi.service.ProductService
 * @see com.ws101.deleon.ecommerceapi.controller.ProductController
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    /**
     * Unique identifier for the product (Primary Key).
     * Auto-generated using database identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Name of the product.
     * Required field with minimum length validation.
     */
    @NotBlank(message = "Product name is required")
    @Column(nullable = false, length = 255)
    private String name;
    
    /**
     * Detailed description of the product.
     */
    @NotBlank(message = "Product description is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    /**
     * Price of the product in the catalog.
     * Must be a positive number.
     */
    @NotNull(message = "Product price is required")
    @Positive(message = "Product price must be greater than zero")
    @Column(nullable = false)
    private Double price;
    
    /**
     * Category this product belongs to.
     * Many-to-One relationship with Category entity.
     */
    @NotNull(message = "Product category is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    /**
     * Available stock quantity for the product.
     * Must be a non-negative integer.
     */
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Column(nullable = false)
    private Integer stockQuantity;
    
    /**
     * URL to the product image (optional).
     * Can be null if no image is available.
     */
    @Column(length = 512)
    private String imageUrl;
}