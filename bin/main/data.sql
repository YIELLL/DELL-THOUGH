-- Sample data for testing the e-commerce API
-- This file will be executed automatically when the application starts

-- Insert sample categories
INSERT INTO categories (name, description) VALUES
('Electronics', 'Electronic devices and gadgets'),
('Clothing', 'Apparel and fashion items'),
('Books', 'Books and educational materials'),
('Home & Garden', 'Home improvement and gardening supplies');

-- Insert sample products
INSERT INTO products (name, description, price, category_id, stock_quantity, image_url) VALUES
('MacBook Pro 14-inch', 'Powerful laptop for professionals', 1999.99, 1, 10, 'https://example.com/images/macbook.jpg'),
('Sony WH-1000XM5', 'Premium noise-cancelling headphones', 349.99, 1, 25, 'https://example.com/images/sony-headphones.jpg'),
('iPad Air', 'Versatile tablet for work and entertainment', 599.99, 1, 15, 'https://example.com/images/ipad.jpg'),
('Nike Air Max', 'Comfortable running shoes', 129.99, 2, 50, 'https://example.com/images/nike-shoes.jpg'),
('Levi\'s Jeans', 'Classic denim jeans', 89.99, 2, 30, 'https://example.com/images/levis-jeans.jpg'),
('The Pragmatic Programmer', 'Essential software development book', 49.99, 3, 20, 'https://example.com/images/pragmatic-programmer.jpg'),
('Clean Code', 'A handbook of agile software craftsmanship', 39.99, 3, 15, 'https://example.com/images/clean-code.jpg'),
('Garden Hose', 'Durable garden watering hose', 29.99, 4, 40, 'https://example.com/images/garden-hose.jpg'),
('Plant Pot Set', 'Set of decorative plant pots', 24.99, 4, 35, 'https://example.com/images/plant-pots.jpg');