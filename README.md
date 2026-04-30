# EcommerceApi - RESTful Product Catalog API

A Spring Boot REST API backend for an e-commerce project demonstrating HTTP fundamentals, REST principles, CRUD operations, and persistent database integration using Spring Data JPA.

## Project Overview

This project implements a RESTful API for managing a product catalog using Spring Boot framework. It demonstrates proper use of HTTP methods, status codes, headers, REST design principles, and integration with relational databases through JPA/Hibernate.

### Technology Stack

- **Framework**: Spring Boot 4.0.5
- **Build Tool**: Gradle
- **Language**: Java 22+
- **Persistence**: Spring Data JPA, Hibernate
- **Database**: MySQL 8.0
- **Dependencies**: Spring Web, Spring Data JPA, Lombok, Validation

### Project Structure

```
EcommerceApi/
├── src/
│   ├── main/
│   │   ├── java/com/ws101/deleon/ecommerceapi/
│   │   │   ├── EcommerceApiApplication.java
│   │   │   ├── config/
│   │   │   │   └── WebConfig.java
│   │   │   ├── controller/
│   │   │   │   └── ProductController.java
│   │   │   ├── service/
│   │   │   │   └── ProductService.java
│   │   │   ├── model/
│   │   │   │   ├── Product.java
│   │   │   │   ├── Category.java
│   │   │   │   ├── Order.java
│   │   │   │   └── OrderItem.java
│   │   │   ├── repository/
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── CategoryRepository.java
│   │   │   │   └── OrderRepository.java
│   │   │   └── exception/
│   │   │       ├── GlobalExceptionHandler.java
│   │   │       └── ProductNotFoundException.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           ├── index.html
│   │           ├── styles.css
│   │           └── script.js
│   └── test/...
├── build.gradle
├── settings.gradle
└── README.md
```

## Database Setup

### Prerequisites

- MySQL 8.0 or higher installed and running
- Database user with create/drop/alter privileges

### Database Configuration

1. Update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
   spring.datasource.username=root
   spring.datasource.password=root
   spring.jpa.hibernate.ddl-auto=update
   ```

2. Spring Boot will automatically create the database and tables on first run

### Database Schema

#### Tables

**categories** (Parent table)
```sql
CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(500)
);
```

**products** (Child of categories)
```sql
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DOUBLE NOT NULL,
    category_id BIGINT NOT NULL,
    stock_quantity INT NOT NULL,
    image_url VARCHAR(512),
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);
```

**orders** (Parent table)
```sql
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    total_amount DOUBLE NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

**order_items** (Child of orders, references products)
```sql
CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    unit_price DOUBLE NOT NULL,
    quantity INT NOT NULL,
    subtotal DOUBLE NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);
```

#### Entity Relationships

- **One-to-Many**: Category → Product (One category has many products)
- **One-to-Many**: Order → OrderItem (One order has many items)
- **References**: OrderItem references Product (for historical records)

## Setup Instructions

### Prerequisites

- Java Development Kit (JDK) 22 or higher
- Gradle (optional - wrapper is included)
- MySQL 8.0 or higher

### Running the Application

1. **Start MySQL**: Ensure MySQL server is running on `localhost:3306`

2. **Using Gradle Wrapper (Linux/Mac)**:
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

- **No Authentication**: API endpoints are not secured
- **No Pagination**: All products are returned in a single response
- **No Advanced Filtering**: Limited to basic category and price range filtering
- **No Image Upload**: Images are referenced by URL only

## Frontend Implementation

The project includes a complete frontend implementation using vanilla JavaScript and the Fetch API:

### Features
- **Product Catalog Display**: Grid layout with product cards
- **Real-time Filtering**: Filter by category and price range
- **Responsive Design**: Mobile-friendly layout
- **Error Handling**: User-friendly error messages
- **Loading States**: Visual feedback during API calls

### Technologies Used
- **HTML5**: Semantic markup and structure
- **CSS3**: Responsive design with Flexbox/Grid
- **JavaScript (ES6+)**: Async/await with Fetch API
- **Fetch API**: Modern HTTP client for REST API consumption

### Key Frontend Files
- `index.html` - Main HTML structure
- `styles.css` - Responsive styling
- `script.js` - Fetch API implementation and DOM manipulation

## Database Setup

### Prerequisites
- MySQL 8.0 or higher installed
- Database user with appropriate permissions

### Database Configuration
1. Create the database:
```sql
CREATE DATABASE ecommerce_db;
```

2. Update `application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. The application will automatically create tables using JPA/Hibernate.

### Database Schema

#### Products Table
```sql
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category VARCHAR(100) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## Running the Application

### Backend (Spring Boot)
```bash
# Build the application
./gradlew build

# Run the application
./gradlew bootRun
```

The API will be available at: `http://localhost:8080`

### Frontend
Open `index.html` in a web browser or serve it through a web server.

### Full Stack Testing
1. Start the Spring Boot application
2. Open the frontend in a browser
3. Test CRUD operations through the UI

## Git Workflow

### Branch Strategy
- `main` - Production-ready code
- `feature/*` - Feature development branches

### Commit Message Format
```
<Type>: <action phrase describing what was implemented>

Examples:
- feat: implemented JPA database integration
- feat: added Fetch API frontend implementation
- fix: resolved CORS configuration issues
- chore: updated README with database setup instructions
```

### Common Git Commands
```bash
# Check status
git status

# Add changes
git add .

# Commit changes
git commit -m "feat: implemented database integration with JPA"

# Push to remote
git push origin feature/database-integration

# Switch to main branch
git checkout main

# Merge feature branch
git merge feature/database-integration

# Delete feature branch
git branch -d feature/database-integration
```

## Authors

- **Kent Jeanne S. De Leon** - Primary Developer
- **Keniel Drew D. De Asis** - Partner Developer

## License

This project is for educational purposes as part of Laboratory 7 - HTTP Fundamentals and Spring Boot.

## Version

2.0.0 - Database Integration and Frontend Implementation
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
