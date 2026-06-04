# 🚀 CoCoBizz - Smart Inventory & Supply Chain Management System

## 📌 Overview

CoCoBizz is a Full-Stack Inventory and Supply Chain Management System designed to streamline inventory tracking, warehouse operations, product management, procurement, shipment tracking, and business analytics.

The platform enables multiple stakeholders such as Admins, Farmers, Sellers, Distributors, and Drivers to collaborate through a secure role-based system.

Built using Spring Boot, React JS, MySQL, Hibernate, JPA, and Spring Security, CoCoBizz provides real-time inventory monitoring, shipment tracking, and business management through modern dashboards.

---

## 🌟 Key Features

### 🔐 Authentication & Authorization

- Secure Login & Registration
- Spring Security Integration
- BCrypt Password Encryption
- Role-Based Access Control (RBAC)
- Protected APIs
- Session Management

---

## 👨‍💼 Admin Module

- Dashboard Analytics
- User Management
- Category Management
- Product Management
- Warehouse Management
- Purchase Management
- Inventory Monitoring
- Shipment Assignment
- Shipment Tracking
- Revenue & Profit Analysis

---

## 👨‍🌾 Farmer Module

- Product Stock Inflow
- Earnings Dashboard
- Sales Analytics
- Product Management
- Delivery Center Tracking
- Payment Tracking

---

## 🏪 Seller Module

- Product Management
- Order Management
- Revenue Monitoring
- Payment Tracking
- Sales Dashboard

---

## 🚚 Distributor Module

- Vehicle Management
- Shipment Requests
- Delivery Tracking
- Live Shipment Monitoring

---

## 📦 Inventory Management

- Real-Time Stock Tracking
- Low Stock Monitoring
- Inventory Valuation
- Warehouse-wise Inventory
- Product Allocation
- Stock Distribution

---

## 🚛 Shipment Management

- Shipment Assignment
- Driver Allocation
- Live Shipment Tracking
- Delivery Status Updates
- Route Monitoring
- Shipment Lifecycle Tracking

---

## 📊 Business Analytics

- Revenue Tracking
- Purchase Analysis
- Profit & Loss Monitoring
- Sales Reports
- Inventory Reports
- Warehouse Analytics

---

# 🖼️ Application Screenshots

## Admin Dashboard

![Admin Dashboard](screenshots/admin-dashboard.png)

Features:

- Revenue Tracking
- Purchase Monitoring
- Profit Analysis
- Inventory Status
- Warehouse Overview
- User Management

---

## Seller Dashboard

![Seller Dashboard](screenshots/seller-dashboard.png)

Features:

- Product Overview
- Order Tracking
- Revenue Analytics
- Payment Management

---

## Farmer Dashboard

![Farmer Dashboard](screenshots/farmer-dashboard.png)

Features:

- Product Stock Monitoring
- Earnings Tracking
- Sales Analytics
- Revenue Charts

---

## Distributor Dashboard

![Distributor Dashboard](screenshots/distributor-dashboard.png)

Features:

- Vehicle Management
- Shipment Requests
- Delivery Tracking

---

## Shipment Tracking

![Shipment Tracking](screenshots/shipment-tracking.png)

Features:

- Driver Information
- Warehouse Details
- Product Details
- Delivery Status Tracking

---

## Inventory Dashboard

![Inventory Dashboard](screenshots/inventory-dashboard.png)

Features:

- Inventory Valuation
- Stock Status
- Warehouse Inventory
- Profit Tracking

---

## Distributor Shipment Requests

![Shipment Requests](screenshots/shipment-requests.png)

Features:

- Accept Shipment
- Reject Shipment
- Delivery Updates
- Route Tracking

---

# 🏗️ System Architecture

```text
React JS Frontend
        │
        ▼
 REST APIs (Axios)
        │
        ▼
 Spring Boot Backend
        │
        ▼
 Spring Security
        │
        ▼
 JPA Repository
        │
        ▼
 Hibernate ORM
        │
        ▼
 MySQL Database
```

---

# ⚙️ Technology Stack

## Frontend

- React JS
- HTML5
- CSS3
- Bootstrap
- JavaScript
- Axios

## Backend

- Java
- Spring Boot
- Spring Framework
- Spring Security
- REST APIs
- Hibernate
- JPA Repository

## Database

- MySQL

## Tools

- IntelliJ IDEA
- VS Code
- GitHub
- Postman
- Maven

---

# 🔒 Security Implementation

## Authentication

- User Login
- Password Encryption using BCrypt
- Secure User Verification

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

## Authorization

- Role-Based Access Control
- Admin Privileges
- User Restrictions
- Secure API Endpoints

---

# 📂 Project Structure

```text
cocobizz
│
├── frontend
│   ├── React Components
│   ├── Pages
│   ├── Services
│   └── API Integration
│
├── backend
│   ├── Controller
│   ├── Service
│   ├── Repository
│   ├── Entity
│   ├── DTO
│   └── Security
│
└── database
    └── MySQL Schema
```

---

# 🛠️ Installation & Setup

## Clone Repository

```bash
git clone https://github.com/LaxmikantKshemaling/cocobizz.git
```

## Backend Setup

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

## Frontend Setup

```bash
cd frontend
npm install
npm start
```

---

# 🗄️ Database Configuration

Update application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cocobizz
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

# 📈 Future Enhancements

- JWT Authentication
- Email Notifications
- Mobile Application
- Advanced Analytics Dashboard
- AI-Based Demand Forecasting
- Export Reports to Excel/PDF
- Cloud Deployment (AWS)

---

# 🎯 Learning Outcomes

Through this project, I gained hands-on experience in:

- Full Stack Development
- Spring Boot REST APIs
- Spring Security
- Authentication & Authorization
- React JS Development
- Hibernate ORM
- JPA Repository
- Database Design
- Inventory Management Systems
- Supply Chain Workflows

---

# 👨‍💻 Author

**Laxmikant Kshemaling**

GitHub:

https://github.com/LaxmikantKshemaling

Project Repository:

https://github.com/LaxmikantKshemaling/cocobizz

---

# ⭐ If you found this project useful

Please give the repository a Star ⭐ on GitHub.
