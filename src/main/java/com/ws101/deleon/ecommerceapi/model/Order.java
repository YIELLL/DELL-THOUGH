package com.ws101.deleon.ecommerceapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order entity representing a customer order in the e-commerce system.
 * 
 * Supports One-to-Many relationship with OrderItem. One order can have multiple order items.
 * 
 * @author Kent Jeanne S. De Leon
 * @author Keniel Drew D. De Asis
 * @see OrderItem
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    /**
     * Unique identifier for the order (Primary Key).
     * Auto-generated using database identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Customer identifier who placed the order.
     */
    @NotNull(message = "Customer ID is required")
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    /**
     * Total amount of the order.
     */
    @NotNull(message = "Total amount is required")
    @Column(nullable = false)
    private Double totalAmount;
    
    /**
     * Order status (e.g., PENDING, CONFIRMED, SHIPPED, DELIVERED).
     */
    @Column(nullable = false, length = 50)
    private String status = "PENDING";
    
    /**
     * Timestamp when the order was created.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    /**
     * Timestamp when the order was last updated.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    /**
     * One-to-Many relationship with OrderItem.
     * One order has many items. Cascade operations and lazy loading for performance.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();
}
