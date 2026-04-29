package com.ws101.deleon.ecommerceapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Category entity representing a product category in the e-commerce system.
 * 
 * Supports One-to-Many relationship with Product. One category can have multiple products.
 * 
 * @author Kent Jeanne S. De Leon
 * @author Keniel Drew D. De Asis
 * @see Product
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    
    /**
     * Unique identifier for the category (Primary Key).
     * Auto-generated using database identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Name of the category.
     * Required field and must be unique.
     */
    @NotBlank(message = "Category name is required")
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    
    /**
     * Description of the category (optional).
     */
    @Column(length = 500)
    private String description;
}
