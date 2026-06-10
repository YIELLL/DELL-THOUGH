# EcommerceApi - RESTful Product Catalog API
DE ASIS, KENIEL DREW D. &
DE LEON, KENT JEANNE S.

A Spring Boot REST API backend for an e-commerce project demonstrating HTTP fundamentals, REST principles, and CRUD operations with in-memory data storage.

## Project Overview

This project implements a RESTful API for managing a product catalog using Spring Boot framework. It demonstrates proper use of HTTP methods, status codes, headers, and REST design principles.

### Technology Stack

- **Framework**: Spring Boot 4.0.5
- **Build Tool**: Gradle
- **Language**: Java 22+
- **Dependencies**: Spring Web, Lombok

### Project Structure

```
EcommerceApi/
├── src/
│   └── main/
│       ├── java/com/ws101/deleon/ecommerceapi/
│       │   ├── EcommerceApiApplication.java
│       │   ├── controller/
│       │   │   └── ProductController.java
│       │   ├── service/
│       │   │   └── ProductService.java
│       │   ├── model/
│       │   │   └── Product.java
│       │   └── exception/
│       │       ├── GlobalExceptionHandler.java
│       │       └── ProductNotFoundException.java
│       └── resources/
│           └── application.properties
├── build.gradle
├── settings.gradle
└── README.md
```

## Setup Instructions

### Prerequisites

- Java Development Kit (JDK) 22 or higher
- Gradle (optional - wrapper is included)

### Running the Application

1. **Using Gradle Wrapper (Linux/Mac)**:
   ```bash
   ./gradlew bootRun
   ```

2. **Using Gradle Wrapper (Windows)**:
   ```bash
   .\gradlew.bat bootRun
   ```

3. **Using Gradle (if installed)**:
   ```bash
   gradle bootRun
   ```

The application will start on `http://localhost:8080`

## Spring Guide Resources

This project follows the structure and REST-service patterns from the official Spring guides:

- https://spring.io/guides/gs/spring-boot/
- https://spring.io/guides/gs/rest-service
- https://spring.io/quickstart/

The code uses Spring Boot starter web, controller-based REST endpoints, request validation, and an in-memory product catalog for quick development and testing.

## API Endpoint Reference

### Base URL
```
http://localhost:8080/api/v1/products
```

### Endpoints

| Method | Path | Description | Status Codes |
|--------|------|-------------|--------------|
| GET | `/api/v1/products` | Retrieve all products | 200 OK |
| GET | `/api/v1/products/{id}` | Get product by ID | 200 OK, 404 Not Found |
| GET | `/api/v1/products/filter?filterType=<type>&filterValue=<value>` | Filter products | 200 OK, 400 Bad Request |
| POST | `/api/v1/products` | Create new product | 201 Created, 400 Bad Request |
| PUT | `/api/v1/products/{id}` | Replace entire product | 200 OK, 400 Bad Request, 404 Not Found |
| PATCH | `/api/v1/products/{id}` | Partially update product | 200 OK, 404 Not Found |
| DELETE | `/api/v1/products/{id}` | Remove product | 204 No Content, 404 Not Found |

### Filter Types

- **category**: Filter by product category (e.g., Electronics, Clothing, Books)
- **name**: Filter by product name (case-insensitive search)
- **price**: Filter by price range (format: `min-max`)

## Security Architecture

This API uses Spring Security with Session-Based Authentication for secure access control.

- **Authentication**: Users log in via form-based authentication at `/login`, establishing an HTTP session.
- **Authorization**: Role-based access control with USER and ADMIN roles.
- **Session Management**: Server-side sessions with automatic CSRF protection.
- **Public Endpoints**: GET `/api/v1/products` and POST `/api/v1/auth/register` are accessible without authentication.
- **Protected Endpoints**: Product management operations require ADMIN role.

## Validation Rules

Input validation is enforced using Bean Validation:

- **Product**:
  - name: Not blank
  - description: Not blank
  - price: Not null, positive
  - category: Not blank
  - stockQuantity: Not null, min 0

- **User Registration**:
  - username: Not blank, 3-20 characters
  - password: Not blank, min 8 characters
  - role: Optional (defaults to USER)

Invalid inputs return 400 Bad Request with detailed error messages.

## Updated API Reference

| Method | Path | Description | Status Codes | Auth Required |
|--------|------|-------------|--------------|---------------|
| GET | `/api/v1/products` | Retrieve all products | 200 OK | No |
| GET | `/api/v1/products/{id}` | Get product by ID | 200 OK, 404 Not Found | No |
| GET | `/api/v1/products/filter?filterType=<type>&filterValue=<value>` | Filter products | 200 OK, 400 Bad Request | No |
| POST | `/api/v1/products` | Create new product | 201 Created, 400 Bad Request | ADMIN |
| PUT | `/api/v1/products/{id}` | Replace entire product | 200 OK, 400 Bad Request, 404 Not Found | ADMIN |
| PATCH | `/api/v1/products/{id}` | Partially update product | 200 OK, 404 Not Found | ADMIN |
| DELETE | `/api/v1/products/{id}` | Remove product | 204 No Content, 404 Not Found | ADMIN |
| POST | `/api/v1/auth/register` | Register new user | 201 Created, 400 Bad Request | No |
| POST | `/login` | Login (form-based) | 302 Redirect, 401 Unauthorized | No |
| POST | `/logout` | Logout | 302 Redirect | Yes |

## Sample Request/Response Examples

### 1. Get All Products

**Request:**
```http
GET http://localhost:8080/api/v1/products
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "MacBook Pro 14-inch",
    "description": "Apple MacBook Pro with M3 chip, 16GB RAM, 512GB SSD",
    "price": 1999.99,
    "category": "Electronics",
    "stockQuantity": 25,
    "imageUrl": "https://example.com/images/macbook.jpg"
  }
]
```

### 2. Get Product by ID

**Request:**
```http
GET http://localhost:8080/api/v1/products/1
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "MacBook Pro 14-inch",
  "description": "Apple MacBook Pro with M3 chip, 16GB RAM, 512GB SSD",
  "price": 1999.99,
  "category": "Electronics",
  "stockQuantity": 25,
  "imageUrl": "https://example.com/images/macbook.jpg"
}
```

### 3. Create Product

**Request:**
```http
POST http://localhost:8080/api/v1/products
Content-Type: application/json

{
  "name": "New Product",
  "description": "Product description",
  "price": 99.99,
  "category": "Electronics",
  "stockQuantity": 50,
  "imageUrl": "https://example.com/image.jpg"
}
```

**Response (201 Created):**
```json
{
  "id": 11,
  "name": "New Product",
  "description": "Product description",
  "price": 99.99,
  "category": "Electronics",
  "stockQuantity": 50,
  "imageUrl": "https://example.com/image.jpg"
}
```

### 4. Update Product (PUT)

**Request:**
```http
PUT http://localhost:8080/api/v1/products/1
Content-Type: application/json

{
  "name": "MacBook Pro 14-inch (Updated)",
  "description": "Updated description",
  "price": 2099.99,
  "category": "Electronics",
  "stockQuantity": 20,
  "imageUrl": "https://example.com/images/macbook-updated.jpg"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "MacBook Pro 14-inch (Updated)",
  "description": "Updated description",
  "price": 2099.99,
  "category": "Electronics",
  "stockQuantity": 20,
  "imageUrl": "https://example.com/images/macbook-updated.jpg"
}
```

### 5. Partially Update Product (PATCH)

**Request:**
```http
PATCH http://localhost:8080/api/v1/products/1
Content-Type: application/json

{
  "price": 1899.99
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "MacBook Pro 14-inch (Updated)",
  "description": "Updated description",
  "price": 1899.99,
  "category": "Electronics",
  "stockQuantity": 20,
  "imageUrl": "https://example.com/images/macbook-updated.jpg"
}
```

### 6. Delete Product

**Request:**
```http
DELETE http://localhost:8080/api/v1/products/1
```

**Response:** 204 No Content

### 7. Filter by Category

**Request:**
```http
GET http://localhost:8080/api/v1/products/filter?filterType=category&filterValue=Electronics
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "MacBook Pro 14-inch",
    "price": 1999.99,
    "category": "Electronics",
    ...
  },
  {
    "id": 2,
    "name": "Sony WH-1000XM5",
    "price": 349.99,
    "category": "Electronics",
    ...
  }
]
```

### 8. Filter by Price Range

**Request:**
```http
GET http://localhost:8080/api/v1/products/filter?filterType=price&filterValue=100-500
```

### 9. Error Response - Product Not Found

**Request:**
```http
GET http://localhost:8080/api/v1/products/999
```

**Response (404 Not Found):**
```json
{
  "timestamp": "2026-04-24T10:30:00",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Product with ID 999 not found"
}
```

### 10. Error Response - Bad Request

**Request:**
```http
POST http://localhost:8080/api/v1/products
Content-Type: application/json

{
  "name": "",
  "price": -10,
  "category": ""
}
```

**Response (400 Bad Request):**
```json
{
  "timestamp": "2026-04-24T10:30:00",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Invalid request data"
}
```

## Input Validation Rules

- **Product Name**: Required, minimum 1 character
- **Price**: Must be a positive number (> 0)
- **Category**: Required, non-empty
- **Stock Quantity**: Must be non-negative (>= 0)

## HTTP Status Codes Used

| Status Code | Usage |
|-------------|-------|
| 200 OK | Successful GET requests |
| 201 Created | Successful POST (creation) |
| 204 No Content | Successful DELETE |
| 400 Bad Request | Invalid request data |
| 404 Not Found | Product not found |
| 500 Internal Server Error | Server error |

## Known Limitations

- **In-Memory Storage**: Data is stored in a temporary ArrayList and will be lost when the application restarts
- **No Database**: No persistent storage mechanism implemented
- **No Authentication**: API endpoints are not secured
- **No Pagination**: All products are returned in a single response

## Git Workflow

### Branch Strategy
- `main` - Production-ready code
- `feature/*` - Feature development branches

### Commit Message Format
```
<Type>: <action phrase describing what was implemented>

Examples:
- feat: implemented product filtering by price range
- fix: resolved getAllProducts() returning null values
- chore: added sample product data initialization
```

### Common Git Commands
```bash
# Check status
git status

# Add changes
git add .

# Commit changes
git commit -m "feat: implemented product CRUD operations"

# Push to remote
git push origin feature/product-api

# Switch to main branch
git checkout main

# Merge feature branch
git merge feature/product-api

# Delete feature branch
git branch -d feature/product-api
```

## Authors

- **Kent Jeanne S. De Leon** - Primary Developer
- **Keniel Drew D. De Asis** - Partner Developer

## License

This project is for educational purposes as part of Laboratory 7 - HTTP Fundamentals and Spring Boot.

## Version

1.0.0 - Initial Release

## Input Validation Rules

- **Product Name**: Required, minimum 1 character
- **Price**: Must be a positive number (> 0)
- **Category**: Required, non-empty
- **Stock Quantity**: Must be non-negative (>= 0)

## HTTP Status Codes Used

| Status Code | Usage |
|-------------|-------|
| 200 OK | Successful GET requests |
| 201 Created | Successful POST (creation) |
| 204 No Content | Successful DELETE |
| 400 Bad Request | Invalid request data |
| 404 Not Found | Product not found |
| 500 Internal Server Error | Server error |

## Known Limitations

- **In-Memory Storage**: Data is stored in a temporary ArrayList and will be lost when the application restarts
- **No Database**: No persistent storage mechanism implemented
- **No Authentication**: API endpoints are not secured
- **No Pagination**: All products are returned in a single response

## Git Workflow

### Branch Strategy
- `main` - Production-ready code
- `feature/*` - Feature development branches

### Commit Message Format
```
<Type>: <action phrase describing what was implemented>

Examples:
- feat: implemented product filtering by price range
- fix: resolved getAllProducts() returning null values
- chore: added sample product data initialization
```

### Common Git Commands
```bash
# Check status
git status

# Add changes
git add .

# Commit changes
git commit -m "feat: implemented product CRUD operations"

# Push to remote
git push origin feature/product-api

# Switch to main branch
git checkout main

# Merge feature branch
git merge feature/product-api

# Delete feature branch
git branch -d feature/product-api
```

## Authors

- **Kent Jeanne S. De Leon** - Primary Developer
- **Keniel Drew D. De Asis** - Partner Developer

## License

This project is for educational purposes as part of Laboratory 7 - HTTP Fundamentals and Spring Boot.

## Version

1.0.0 - Initial Release
=======
# DELL-THOUGH
>>>>>>> 6fc214e6e19e492a07f2cdeea25030dbc168bd59
