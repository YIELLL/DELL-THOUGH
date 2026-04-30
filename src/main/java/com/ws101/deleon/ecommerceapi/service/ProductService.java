package com.ws101.deleon.ecommerceapi.service;

import com.ws101.deleon.ecommerceapi.exception.ProductNotFoundException;
import com.ws101.deleon.ecommerceapi.model.Product;
import com.ws101.deleon.ecommerceapi.repository.CategoryRepository;
import com.ws101.deleon.ecommerceapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for product-related operations.
 * 
 * Provides business logic for managing products in the e-commerce catalog.
 * Uses Spring Data JPA repositories for database persistence.
 * This class acts as an intermediary between the API controller and the
 * data access layer.
 * 
 * @author Kent Jeanne S. De Leon
 * @author Keniel Drew D. De Asis
 * @see Product
 * @see com.ws101.deleon.ecommerceapi.controller.ProductController
 */
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Constructor for dependency injection.
     * 
     * @param productRepository the product data repository
     * @param categoryRepository the category data repository
     */
    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retrieves all products from the catalog.
     * 
     * @return a {@code List<Product>} containing all products.
     *         Returns an empty list if no products exist.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Finds a product by its unique identifier.
     * 
     * @param id the unique identifier of the product to find.
     * @return the product if found.
     * @throws ProductNotFoundException if the product does not exist.
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    /**
     * Creates a new product and adds it to the catalog.
     * 
     * @param product the product to create (ID will be auto-generated).
     * @return the created product with assigned ID.
     */
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Updates an existing product in the catalog.
     * 
     * @param id      the unique identifier of the product to update.
     * @param product the updated product data.
     * @return the updated product.
     * @throws ProductNotFoundException if the product does not exist.
     */
    public Product updateProduct(Long id, Product product) {
        Product existing = getProductById(id);
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setCategory(product.getCategory());
        existing.setStockQuantity(product.getStockQuantity());
        existing.setImageUrl(product.getImageUrl());
        return productRepository.save(existing);
    }

    /**
     * Partially updates an existing product.
     * Only updates fields that are not null in the provided product.
     * 
     * @param id      the unique identifier of the product to patch.
     * @param product the product data with fields to update.
     * @return the updated product.
     * @throws ProductNotFoundException if the product does not exist.
     */
    public Product patchProduct(Long id, Product product) {
        Product existing = getProductById(id);

        if (product.getName() != null) {
            existing.setName(product.getName());
        }
        if (product.getDescription() != null) {
            existing.setDescription(product.getDescription());
        }
        if (product.getPrice() != null) {
            if (product.getPrice() <= 0) {
                throw new IllegalArgumentException("Product price must be greater than zero");
            }
            existing.setPrice(product.getPrice());
        }
        if (product.getCategoryId() != null) {
            // Find the category by ID and set it
            var category = categoryRepository.findById(product.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + product.getCategoryId()));
            existing.setCategory(category);
        }
        if (product.getStockQuantity() != null) {
            if (product.getStockQuantity() < 0) {
                throw new IllegalArgumentException("Stock quantity cannot be negative");
            }
            existing.setStockQuantity(product.getStockQuantity());
        }
        if (product.getImageUrl() != null) {
            existing.setImageUrl(product.getImageUrl());
        }

        return productRepository.save(existing);
    }

    /**
     * Deletes a product from the catalog.
     * 
     * @param id the unique identifier of the product to delete.
     * @throws ProductNotFoundException if the product does not exist.
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    /**
     * Finds products by category.
     * 
     * @param categoryId the category identifier
     * @return list of products in the category
     */
    public List<Product> getProductsByCategory(Long categoryId) {
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));
        return productRepository.findByCategory(category);
    }

    /**
     * Finds products within a price range.
     * 
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return list of products with price between min and max
     */
    public List<Product> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        if (minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("Price values must be non-negative");
        }
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("minPrice must be less than or equal to maxPrice");
        }
        return productRepository.findProductsByPriceRange(minPrice, maxPrice);
    }

    /**
     * Finds products by name (case-insensitive search).
     * 
     * @param name the search term to match against product names
     * @return list of products with matching names
     */
    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Generic filter method that filters products based on filter type and value.
     * 
     * @param filterType  the type of filter (category, price, name).
     * @param filterValue the value to filter by.
     * @return a {@code List<Product>} containing filtered products.
     * @throws IllegalArgumentException if filterType is invalid.
     */
    public List<Product> filterProducts(String filterType, String filterValue) {
        if (filterType == null || filterValue == null) {
            throw new IllegalArgumentException("Filter type and value cannot be null");
        }

        switch (filterType.toLowerCase()) {
            case "category":
                try {
                    Long categoryId = Long.parseLong(filterValue);
                    return getProductsByCategory(categoryId);
                } catch (NumberFormatException e) {
                    return productRepository.findByNameContainingIgnoreCase(filterValue);
                }
            case "name":
                return getProductsByName(filterValue);
            case "price":
                // For price, filterValue should be in format "min-max"
                String[] priceRange = filterValue.split("-");
                if (priceRange.length == 2) {
                    try {
                        double min = Double.parseDouble(priceRange[0].trim());
                        double max = Double.parseDouble(priceRange[1].trim());
                        return getProductsByPriceRange(min, max);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Price filter must contain valid numbers in format: min-max");
                    }
                }
                throw new IllegalArgumentException("Price filter must be in format: min-max");
            default:
                throw new IllegalArgumentException("Invalid filter type: " + filterType + 
                    ". Valid types: category, name, price");
        }
    }

    /**
     * Gets all products that are in stock (stockQuantity > 0).
     * 
     * @return list of products with available stock
     */
    public List<Product> getProductsInStock() {
        return productRepository.findProductsInStock();
    }

    /**
     * Finds products by category and price range combined.
     * 
     * @param categoryId the category identifier
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return list of products matching both criteria
     */
    public List<Product> getProductsByCategoryAndPriceRange(Long categoryId, Double minPrice, Double maxPrice) {
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));
        return productRepository.findByCategoryAndPriceRange(category, minPrice, maxPrice);
    }
        return productRepository.findByCategoryAndPriceRange(categoryId, minPrice, maxPrice);
    }
}