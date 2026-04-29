package com.ws101.deleon.ecommerceapi.repository;

import com.ws101.deleon.ecommerceapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Order entity providing CRUD and custom query operations.
 * 
 * @author Kent Jeanne S. De Leon
 * @author Keniel Drew D. De Asis
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Find all orders for a specific customer.
     * 
     * @param customerId the customer identifier
     * @return list of orders for the customer
     */
    List<Order> findByCustomerId(Long customerId);
    
    /**
     * Find all orders with a specific status.
     * 
     * @param status the order status
     * @return list of orders with the given status
     */
    List<Order> findByStatus(String status);
    
    /**
     * Find orders created within a date range.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return list of orders created between the dates
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    List<Order> findOrdersByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    
    /**
     * Find orders for a customer with a specific status.
     * 
     * @param customerId the customer identifier
     * @param status the order status
     * @return list of orders matching both criteria
     */
    @Query("SELECT o FROM Order o WHERE o.customerId = :customerId AND o.status = :status ORDER BY o.createdAt DESC")
    List<Order> findCustomerOrdersByStatus(
            @Param("customerId") Long customerId,
            @Param("status") String status
    );
}
