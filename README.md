# CoCoBizz - Inventory Management System

## Overview

CoCoBizz is a full-stack Inventory Management System developed to manage products, inventory, orders, warehouses, and business operations efficiently. The application provides secure authentication and authorization, inventory tracking, product management, and order management through a modern web interface.

## Features

* User Authentication and Authorization using Spring Security
* Secure Login and Registration
* Role-Based Access Control (Admin/User)
* Product Management
* Inventory Tracking
* Warehouse Management
* Order Management
* Stock Monitoring
* Dashboard for Business Operations
* Responsive User Interface
* REST API Integration
* MySQL Database Integration

## Technologies Used

### Frontend

* React JS
* HTML
* CSS
* Bootstrap
* Axios

### Backend

* Java
* Spring Boot
* Spring Framework
* Spring Security
* REST APIs
* JPA Repository
* Hibernate

### Database

* MySQL

### Tools

* IntelliJ IDEA
* VS Code
* GitHub
* Postman

## Security Implementation

The application uses Spring Security for securing APIs and application resources.

### Authentication

* User Login
* Password Encryption using BCryptPasswordEncoder
* Secure User Verification

### Authorization

* Role-Based Access Control
* Admin Access Management
* User Access Restrictions

Example Security Configuration:

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

## Modules

### Product Management

* Add Product
* Update Product
* Delete Product
* View Products

### Inventory Management

* Stock Tracking
* Inventory Updates
* Inventory Reports

### Order Management

* Create Orders
* Manage Orders
* Track Orders

### Warehouse Management

* Manage Warehouses
* Product Allocation
* Stock Distribution

## Project Architecture

Frontend (React JS)

↓

REST APIs

↓

Spring Boot Backend

↓

JPA Repository / Hibernate

↓

MySQL Database

## Database

MySQL is used to store:

* User Information
* Product Details
* Inventory Records
* Orders
* Warehouse Data

## Key Learning Outcomes

* Full Stack Application Development
* Spring Boot REST API Development
* Spring Security Authentication & Authorization
* React JS Frontend Development
* Database Design using MySQL
* JPA Repository and Hibernate Integration
* Secure Application Development

## Future Enhancements

* JWT Authentication
* Email Notifications
* Sales Reports
* Advanced Analytics Dashboard
* Export Reports to Excel/PDF

## Author

Laxmikant Kshemaling

GitHub:
https://github.com/LaxmikantKshemaling

Project Repository:
https://github.com/LaxmikantKshemaling/cocobizz
