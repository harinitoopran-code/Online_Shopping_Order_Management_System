CREATE DATABASE IF NOT EXISTS online_shop;
USE online_shop;
CREATE TABLE IF NOT EXISTS users (
 user_id INT AUTO_INCREMENT PRIMARY KEY, full_name VARCHAR(100) NOT NULL,
 email VARCHAR(120) NOT NULL UNIQUE, password_hash VARCHAR(255) NOT NULL,
 role ENUM('CUSTOMER','ADMIN') NOT NULL DEFAULT 'CUSTOMER',
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS products (
 product_id INT AUTO_INCREMENT PRIMARY KEY, product_name VARCHAR(150) NOT NULL,
 category VARCHAR(80), description TEXT, price DECIMAL(10,2) NOT NULL,
 stock_qty INT NOT NULL DEFAULT 0, active BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE IF NOT EXISTS carts (
 cart_id INT AUTO_INCREMENT PRIMARY KEY, user_id INT NOT NULL UNIQUE,
 FOREIGN KEY(user_id) REFERENCES users(user_id)
);
CREATE TABLE IF NOT EXISTS cart_items (
 cart_item_id INT AUTO_INCREMENT PRIMARY KEY, cart_id INT NOT NULL,
 product_id INT NOT NULL, quantity INT NOT NULL,
 UNIQUE(cart_id,product_id),
 FOREIGN KEY(cart_id) REFERENCES carts(cart_id) ON DELETE CASCADE,
 FOREIGN KEY(product_id) REFERENCES products(product_id)
);
CREATE TABLE IF NOT EXISTS orders (
 order_id INT AUTO_INCREMENT PRIMARY KEY, user_id INT NOT NULL,
 order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, delivery_address TEXT NOT NULL,
 total_amount DECIMAL(10,2) NOT NULL, order_status VARCHAR(30) DEFAULT 'PLACED',
 FOREIGN KEY(user_id) REFERENCES users(user_id)
);
CREATE TABLE IF NOT EXISTS order_items (
 order_item_id INT AUTO_INCREMENT PRIMARY KEY, order_id INT NOT NULL,
 product_id INT NOT NULL, product_name_snapshot VARCHAR(150) NOT NULL,
 unit_price DECIMAL(10,2) NOT NULL, quantity INT NOT NULL,
 FOREIGN KEY(order_id) REFERENCES orders(order_id),
 FOREIGN KEY(product_id) REFERENCES products(product_id)
);
INSERT INTO products(product_name,category,description,price,stock_qty)
SELECT 'Laptop','Electronics','Sample laptop',45000,10
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_name='Laptop');
INSERT INTO products(product_name,category,description,price,stock_qty)
SELECT 'Wireless Mouse','Electronics','USB mouse',650,40
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_name='Wireless Mouse');
INSERT INTO products(product_name,category,description,price,stock_qty)
SELECT 'Notebook','Stationery','A4 notebook',80,100
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_name='Notebook');
