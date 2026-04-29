package com.ws101.deleon.ecommerceapi.service;

import com.ws101.deleon.ecommerceapi.exception.ProductNotFoundException;
import com.ws101.deleon.ecommerceapi.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for product-related operations.
 * 
 * Provides business logic for managing products in the e-commerce catalog.
 * This class acts as an intermediary between the API controller and the
 * data access layer, using in-memory storage for demonstration purposes.
 * 
 * @author Kent Jeanne S. De Leon
 * @author Keniel Drew D. De Asis
 * @see Product
 * @see ProductController
 */
@Service
public class ProductService {

    /**
     * In-memory storage for products using ArrayList.
     * This simulates a database for demonstration purposes.
     */
    private final List<Product> productList = new ArrayList<>();

    /**
     * Counter for generating unique product IDs.
     * Uses the size of the list to ensure uniqueness.
     */
    private Long idCounter = 1L;

    /**
     * Constructor that initializes the service with sample product data.
     * Loads at least 10 sample products into the in-memory storage.
     */
    public ProductService() {
        initializeSampleData();
    }

    /**
     * Initializes the product catalog with sample data.
     * Creates 10 sample products across different categories.
     */
    private void initializeSampleData() {
        // Electronics category
        productList.add(new Product(1L, "MacBook Pro 14-inch", 
            "Apple MacBook Pro with M3 chip, 16GB RAM, 512GB SSD", 
            1999.99, "Electronics", 25, "https://example.com/images/macbook.jpg"));
        
        productList.add(new Product(2L, "Sony WH-1000XM5", 
            "Premium noise-canceling wireless headphones", 
            349.99, "Electronics", 50, "https://example.com/images/sony-headphones.jpg"));
        
        productList.add(new Product(3L, "Samsung Galaxy S24 Ultra", 
            "Latest flagship smartphone with AI features", 
            1299.99, "Electronics", 30, "https://example.com/images/galaxy-s24.jpg"));
        
        // Clothing category
        productList.add(new Product(4L, "Nike Air Max 270", 
            "Classic running shoes with Air cushioning", 
            149.99, "Clothing", 100, "https://example.com/images/nike-shoes.jpg"));
        
        productList.add(new Product(5L, "Adidas Originals Hoodie", 
            "Comfortable cotton hoodie with iconic三条纹", 
            79.99, "Clothing", 75, "https://example.com/images/adidas-hoodie.jpg"));
        
        productList.add(new Product(6L, "Levi's 501 Jeans", 
            "Classic straight-fit jeans", 
            89.99, "Clothing", 60, "https://example.com/images/levis-jeans.jpg"));
        
        // Books category
        productList.add(new Product(7L, "The Pragmatic Programmer", 
            "Your journey to mastery - 20th Anniversary Edition", 
            49.99, "Books", 40, "https://example.com/images/pragmatic-programmer.jpg"));
        
        productList.add(new Product(8L, "Clean Code", 
            "A handbook of agile software craftsmanship", 
            39.99, "Books", 55, "https://example.com/images/clean-code.jpg"));
        
        // Home & Garden category
        productList.add(new Product(9L, "Dyson V15 Detect", 
            "Cordless vacuum with laser dust detection", 
            749.99, "Home & Garden", 15, "https://example.com/images/dyson-v15.jpg"));
        
        productList.add(new Product(10L, "IKEA KALLAX Shelf", 
            "Versatile shelving unit for storage and display", 
            129.99, "Home & Garden", 35, "https://example.com/images/kallax.jpg"));
        
        // Update the counter to continue from the last ID
        idCounter = 11L;
    }

    /**
     * Retrieves all products from the catalog.
     * 
     * @return a {@code List<Product>} containing all products.
     *         Returns an empty list if no products exist.
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(productList);
    }

    /**
     * Finds a product by its unique identifier.
     * 
     * @param id the unique identifier of the product to find.
     * @return the product if found.
     * @throws ProductNotFoundException if the product does not exist.
     */
    public Product getProductById(Long id) {
        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    /**
     * Creates a new product and adds it to the catalog.
     * 
     * @param product the product to create (ID will be auto-generated).
     * @return the created product with assigned ID.
     */
    public Product createProduct(Product product) {
        product.setId(idCounter++);
        productList.add(product);
        return product;
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
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getId().equals(id)) {
                product.setId(id);
                productList.set(i, product);
                return product;
            }
        }
        throw new ProductNotFoundException("Product not found with id: " + id);
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
        if (product.getCategory() != null) {
            existing.setCategory(product.getCategory());
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

        return existing;
    }

    /**
     * Deletes a product from the catalog.
     * 
     * @param id the unique identifier of the product to delete.
     * @return {@code true} if the product was found and deleted,
     *         {@code false} if the product was not found.
     */
    public void deleteProduct(Long id) {
        boolean removed = productList.removeIf(product -> product.getId().equals(id));
        if (!removed) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
    }

    /**
     * Filters products by category.
     * 
     * Retrieves all products belonging to the specified category.
     * 
     * @param category the category to filter by.
     * @return a {@code List<Product>} containing all products in the category.
     *         Returns an empty list if no products match.
     */
    public List<Product> filterProductWithCategory(String category) {
        return productList.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Filters products by price range.
     * 
     * Retrieves all products where the price falls within the specified range,
     * inclusive of both boundaries.
     * 
     * @param minPrice the minimum price threshold (inclusive).
     *                  Must be non-negative and less than or equal to maxPrice.
     * @param maxPrice the maximum price threshold (inclusive).
     *                  Must be non-negative and greater than or equal to minPrice.
     * @return a {@code List<Product>} containing all products with price within
     *         [minPrice, maxPrice]. Returns an empty list if no products match.
     * @throws IllegalArgumentException if minPrice is negative, maxPrice is
     *         negative, or minPrice > maxPrice
     */
    public List<Product> filterProductWithPrice(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("Price values must be non-negative");
        }
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("minPrice must be less than or equal to maxPrice");
        }
        
        return productList.stream()
                .filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    /**
     * Filters products by name (case-insensitive search).
     * 
     * Retrieves all products whose names contain the specified search term.
     * 
     * @param name the search term to match against product names.
     * @return a {@code List<Product>} containing all products with matching names.
     *         Returns an empty list if no products match.
     */
    public List<Product> filterProductWithName(String name) {
        return productList.stream()
                .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
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
                return filterProductWithCategory(filterValue);
            case "name":
                return filterProductWithName(filterValue);
            case "price":
                // For price, filterValue should be in format "min-max"
                String[] priceRange = filterValue.split("-");
                if (priceRange.length == 2) {
                    double min = Double.parseDouble(priceRange[0].trim());
                    double max = Double.parseDouble(priceRange[1].trim());
                    return filterProductWithPrice(min, max);
                }
                throw new IllegalArgumentException("Price filter must be in format: min-max");
            default:
                throw new IllegalArgumentException("Invalid filter type: " + filterType + 
                    ". Valid types: category, name, price");
        }
    }
}