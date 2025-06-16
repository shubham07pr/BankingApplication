# 🏦 Banking Application (Spring Boot)

This is a full-stack backend **Banking Management System** developed using **Spring Boot**. The application supports account creation, secure login with **JWT authentication**, balance enquiry, credit/debit/transfer functionalities, PDF bank statement generation, and email notifications using **JavaMailSender**.

---

## ✨ Features

- 🔐 JWT-based Authentication & Authorization
- 👤 Account creation with role-based access
- 💸 Credit, Debit, and Transfer operations
- 📬 Email Notifications for transactions
- 📊 Swagger API Documentation (`springdoc-openapi`)
- 🗃️ MySQL Database Integration
- 📈 Transaction History Tracking

---

## 🛠️ Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **MySQL**
- **JPA / Hibernate**
- **iText (PDF Generation)**
- **JavaMailSender**
- **Swagger UI** (`springdoc-openapi`)

---

## 🧑‍💻 Modules

- **Authentication Module** – JWT-based login/signup.
- **User Module** – CRUD operations for user accounts.
- **Transaction Module** – Credit, Debit, Transfer, and view transactions.
- **Statement Module** – Generate PDF statements.
- **Email Service** – Sends confirmation and alert emails.

---

## 📂 Project Structure

com.bankingsystem
├── controller
├── service
├── model
├── repository
├── config
├── security
└── dto
