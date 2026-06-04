# CoCoBizz - Inventory Management System

## Overview

CoCoBizz is a full-stack Inventory Management System developed to manage products, inventory, orders, warehouses, and business operations efficiently. The application provides secure authentication and authorization, inventory tracking, product management, order management, shipment tracking, and warehouse management through a modern web interface.

---

## Application Screenshots

### Admin Dashboard

https://github.com/LaxmikantKshemaling/cocobizz/blob/cccd11636450b4052f7e0e67f06fa55ebb5fbdb2/cocobuzz.png

### Seller Dashboard

[![Seller Dashboard(https://github.com/LaxmikantKshemaling/cocobizz/blob/abd5f96b3568e5bec5f9900afcca88bc5cb7a3a0/Screenshot%202026-06-04%20142304.png)

### Farmer Dashboard

[![Farmer Dashboard](screenshots/farmer-dashboard.png)](https://github.com/LaxmikantKshemaling/cocobizz/blob/1c567b9079bdaefcbaf3f0cfbc788db55651d502/Screenshot%202026-06-04%20142120.png)

### Inventory Dashboard

![Inventory Dashboard](screenshots/inventory-dashboard.png)

### Live Shipment Tracking

![Shipment Tracking](screenshots/shipment-tracking.png)

### Distributor Dashboard

![Distributor Dashboard](screenshots/distributor-dashboard.png)

### Distributor Shipment Requests

![Shipment Requests](screenshots/shipment-requests.png)

---

## Features

### User Authentication and Authorization

* Spring Security Authentication
* Secure Login and Registration
* Password Encryption using BCryptPasswordEncoder
* Role-Based Access Control (Admin/User/Farmer/Seller/Distributor)

### Product Management

* Add Product
* Update Product
* Delete Product
* View Products
* Product Categorization

### Inventory Tracking

* Real-Time Inventory Monitoring
* Stock Management
* Inventory Reports
* Low Stock Alerts
* Inventory Valuation

### Warehouse Management

* Warehouse Creation
* Product Allocation
* Stock Distribution
* Warehouse Inventory Monitoring

### Order Management

* Create Orders
* Manage Orders
* Track Orders
* Purchase Transactions

### Shipment Management

* Assign Shipments
* Driver Allocation
* Shipment Tracking
* Live Delivery Status
* Delivery Workflow Monitoring

### Dashboard for Business Operations

* Revenue Tracking
* Purchase Monitoring
* Profit & Loss Analysis
* Sales Reports
* Inventory Analytics

### Responsive User Interface

* Modern Dashboard Design
* Responsive Layout
* User-Friendly Navigation

### REST API Integration

* Secure REST APIs
* Frontend-Backend Communication using Axios

### MySQL Database Integration

* Persistent Data Storage
* Optimized Database Operations

---

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
* Maven

---

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

### Example Security Configuration

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

---

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

### Shipment Management

* Shipment Assignment
* Driver Management
* Delivery Tracking
* Live Shipment Monitoring

---

## Project Architecture

```text
Frontend (React JS)

↓

REST APIs

↓

Spring Boot Backend

↓

Spring Security

↓

JPA Repository / Hibernate

↓

MySQL Database
```

---

## Database

MySQL is used to store:

* User Information
* Product Details
* Inventory Records
* Orders
* Warehouse Data
* Shipment Data

---

## Key Learning Outcomes

* Full Stack Application Development
* Spring Boot REST API Development
* Spring Security Authentication & Authorization
* React JS Frontend Development
* Database Design using MySQL
* JPA Repository and Hibernate Integration
* Secure Application Development
* Inventory and Supply Chain Management

---

## Future Enhancements

* JWT Authentication
* Email Notifications
* Sales Reports
* Advanced Analytics Dashboard
* Export Reports to Excel/PDF
* Cloud Deployment
* Mobile Application Support

---

## Author

### Laxmikant Kshemaling

GitHub:
https://github.com/LaxmikantKshemaling

Project Repository:
https://github.com/LaxmikantKshemaling/cocobizz

---

⭐ If you found this project useful, please consider giving it a Star on GitHub.
