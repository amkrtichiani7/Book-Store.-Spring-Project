# 📚 Bookstore Management System

A robust Spring Boot web application designed for managing inventory, client relations, and order processing. This project features a secure multi-role access system, automated server-side validation, and a dynamic order management portal.

## 🚀 Project Overview

The system provides three distinct user experiences:
* **Admin:** Full control over employee management and catalog settings.
* **Employee:** Management of book inventory and client order processing.
* **Client:** A personal portal to browse books, place orders, and track purchase history.



[Image of Spring Boot Web Application Architecture Diagram]


## 🛠 Tech Stack

* **Backend:** Java 17, Spring Boot 3.x, Spring Security
* **Data:** Spring Data JPA, H2 Embedded Database (In-Memory)
* **View:** Thymeleaf, HTML5, CSS3, JavaScript
* **Security:** BCrypt Password Hashing, CSRF Protection
* **Testing:** JUnit 5, MockMvc, AssertJ

---

## 🔑 Getting Started & Credentials

To run the project:
1. Ensure **JDK 17** and **Maven** are installed.
2. Run `mvn spring-boot:run` from the root directory.
3. Access the app at `http://localhost:8084`.

### Test Credentials

| Role | Email | Password | Notes |
| :--- | :--- | :--- | :--- |
| **Admin** | admin@test.com | admin123 | Full system access |
| **Employee** | john.doe@email.com | pass123 | Inventory & Orders |
| **Client** | client1@example.com | password123 | Shopping & Profile |

### Database Access (H2 Console)
* **URL:** `http://localhost:8084/h2-console`
* **JDBC URL:** `jdbc:h2:mem:testdb`
* **User:** `sa` | **Password:** (blank)
* **Security Note:** Access to the H2 Console is restricted. You must be logged in with **Admin** privileges to view the database.

---

## ✨ Key Features

### 🔐 Secure Authentication
Implemented **Spring Security** with a `CustomUserDetailsService` to handle multi-table authentication. Passwords are encrypted using the **BCrypt** hashing algorithm to ensure industry-standard data protection.



### 📦 Dynamic Order Processing
Clients can create complex orders with multiple book items. The system includes a custom JavaScript implementation to dynamically add rows to the order form, which are then bound to a list of DTOs on the server side.

### 📊 Advanced Cataloging
The book catalog supports:
* **Server-side Pagination & Sorting:** Efficiently handles large datasets.
* **Search Filtering:** Real-time filtering by keyword across title, author, and genre.

### 👤 Personal Profile
Clients have a dedicated portal to view their current balance and a detailed history of their purchases, including the specific contact information (email and phone) of the employee who handled their order.

---

## 🧪 Validation & Testing
* **Input Validation:** Uses `@Valid` and JSR-303 annotations to ensure data integrity (e.g., regex-based phone validation).
* **MockMvc Tests:** Comprehensive testing of web endpoints, security redirects, and model attributes.