package com.ws101.deleon.ecommerceapi.controller;

import com.ws101.deleon.ecommerceapi.model.Product;
import com.ws101.deleon.ecommerceapi.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Product API endpoints.
 * 
 * This controller handles all HTTP requests related to product operations.
 * It acts as the bridge between the frontend and the service layer,
 * performing input validation and delegating business logic to ProductService.
 * 
 * @author Kent Jeanne S. De Leon
 * @author Keniel Drew D. De Asis
 * @see ProductService
 * @see Product
 */
@RestController
@RequestMapping("/api/v1/products")
@Validated
public class ProductController {

    /**
     * The product service instance for business logic operations.
     */
    private final ProductService productService;

    /**
     * Constructor for dependency injection of ProductService.
     * 
     * @param productService the service layer for product operations
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieves all products from the catalog.
     * 
     * @return ResponseEntity containing list of all products with HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Retrieves a single product by its unique identifier.
     * 
     * @param id the unique identifier of the product
     * @return ResponseEntity containing the product if found, or HTTP 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Filters products based on specified filter type and value.
     * 
     * @param filterType  the criteria to filter by (category, name, price)
     * @param filterValue the value to filter with
     * @return ResponseEntity containing list of filtered products with HTTP 200 OK
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterProducts(
            @RequestParam String filterType,
            @RequestParam String filterValue) {
        try {
            List<Product> products = productService.filterProducts(filterType, filterValue);
            return ResponseEntity.ok(products);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Creates a new product in the catalog.
     * 
     * @param product the product data from the request body
     * @return ResponseEntity containing the created product with HTTP 201 Created
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * Updates an entire product (replace operation).
     * 
     * @param id      the unique identifier of the product to update
     * @param product the updated product data
     * @return ResponseEntity containing the updated product, or HTTP 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Partially updates a product.
     * 
     * @param id      the unique identifier of the product to patch
     * @param product the product data with fields to update
     * @return ResponseEntity containing the updated product, or HTTP 404 Not Found
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Product> patchProduct(
            @PathVariable Long id,
            @RequestBody Product product) {
        Product updatedProduct = productService.patchProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Deletes a product from the catalog.
     * 
     * @param id the unique identifier of the product to delete
     * @return ResponseEntity with HTTP 204 No Content if successful,
     *         or HTTP 404 Not Found if product doesn't exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}