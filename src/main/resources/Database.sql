CREATE DATABASE product_manager_db;

USE product_manager_db;

CREATE TABLE products (

    id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    supplier VARCHAR(255),
    category VARCHAR(255) NOT NULL

);

CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(255) UNIQUE
);