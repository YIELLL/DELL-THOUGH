package com.ws101.deleon.ecommerceapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OrderItem entity representing a single item within an order.
 * 
 * Child entity in a One-to-Many relationship with Order. Multiple items belong to one order.
 * 
 * @author Kent Jeanne S. De Leon
 * @author Keniel Drew D. De Asis
 * @see Order
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    
    /**
     * Unique identifier for the order item (Primary Key).
     * Auto-generated using database identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Reference to the parent Order.
     * Many-to-One relationship: many items belong to one order.
     * Uses LAZY loading for performance and maintains the foreign key.
     */
    @NotNull(message = "Order is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    /**
     * Product identifier for the item being ordered.
     */
    @NotNull(message = "Product ID is required")
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    /**
     * Product name at the time of order (snapshot for historical records).
     */
    @NotNull(message = "Product name is required")
    @Column(nullable = false, length = 255)
    private String productName;
    
    /**
     * Unit price of the product at the time of order.
     */
    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be greater than zero")
    @Column(nullable = false)
    private Double unitPrice;
    
    /**
     * Quantity of the product in this order item.
     */
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false)
    private Integer quantity;
    
    /**
     * Subtotal for this order item (unitPrice * quantity).
     */
    @Column(nullable = false)
    private Double subtotal;
    
    /**
     * Pre-persist lifecycle method to calculate subtotal.
     */
    @PrePersist
    public void calculateSubtotal() {
        if (this.unitPrice != null && this.quantity != null) {
            this.subtotal = this.unitPrice * this.quantity;
        }
    }
    
    /**
     * Pre-update lifecycle method to recalculate subtotal.
     */
    @PreUpdate
    public void recalculateSubtotal() {
        calculateSubtotal();
    }
}
