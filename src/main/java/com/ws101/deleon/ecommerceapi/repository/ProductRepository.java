package com.ws101.deleon.ecommerceapi.repository;

import com.ws101.deleon.ecommerceapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Product entity providing CRUD and custom query operations.
 * 
 * Extends JpaRepository to inherit standard CRUD methods:
 * save, findById, findAll, delete, deleteById, etc.
 * 
 * @author Kent Jeanne S. De Leon
 * @author Keniel Drew D. De Asis
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * Find all products in a specific category.
     * 
     * @param categoryId the category identifier
     * @return list of products in the category
     */
    List<Product> findByCategoryId(Long categoryId);
    
    /**
     * Search products by name (case-insensitive).
     * 
     * @param name the product name to search for
     * @return list of products matching the name
     */
    List<Product> findByNameIgnoreCase(String name);
    
    /**
     * Find products by name containing a search term (case-insensitive).
     * 
     * @param nameFragment the search term
     * @return list of products containing the term
     */
    List<Product> findByNameContainingIgnoreCase(String nameFragment);
    
    /**
     * Find products within a price range using JPQL.
     * 
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return list of products with price between min and max
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice ORDER BY p.price ASC")
    List<Product> findProductsByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    /**
     * Find products in stock (stockQuantity > 0).
     * 
     * @return list of products with available stock
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity > 0 ORDER BY p.name ASC")
    List<Product> findProductsInStock();
    
    /**
     * Find products by category and price range combined.
     * 
     * @param categoryId the category identifier
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return list of products matching both criteria
     */
    @Query("SELECT p FROM Product p WHERE p.categoryId = :categoryId AND p.price BETWEEN :minPrice AND :maxPrice ORDER BY p.price ASC")
    List<Product> findByCategoryAndPriceRange(
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );
}
