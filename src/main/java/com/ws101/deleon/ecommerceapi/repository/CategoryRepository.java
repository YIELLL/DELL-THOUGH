package com.ws101.deleon.ecommerceapi.repository;

import com.ws101.deleon.ecommerceapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Category entity providing CRUD and custom query operations.
 * 
 * @author Kent Jeanne S. De Leon
 * @author Keniel Drew D. De Asis
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * Find a category by its name.
     * 
     * @param name the category name
     * @return Optional containing the category if found
     */
    Optional<Category> findByName(String name);
    
    /**
     * Check if a category with the given name exists.
     * 
     * @param name the category name
     * @return true if category exists, false otherwise
     */
    boolean existsByName(String name);
}
