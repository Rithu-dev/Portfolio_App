# Stock Portfolio Management System

A full-stack Stock Portfolio Management application built using **Java, Spring Boot, Spring Security, JWT Authentication, MySQL, and HTML/CSS/JavaScript**.

The application allows users to register, log in securely, manage their stock portfolios, and track buy/sell transactions.

## Features

### Authentication & Security

- User registration with password encryption using BCrypt.
- User login with JWT-based authentication.
- Stateless authentication using Spring Security.
- Protected REST APIs.
- Portfolio access restricted to the authenticated user.
- Current user identification through JWT.

### Portfolio Management

- Add stocks to a portfolio.
- Retrieve user-specific portfolio holdings.
- View portfolio summary.
- Track stock quantities and purchase prices.

### Transaction Management

- Buy and sell stocks.
- Maintain transaction history.
- Calculate weighted average purchase price.
- Prevent selling more shares than currently owned.
- Automatically remove a holding when its quantity reaches zero.

### Frontend

- User registration and login pages.
- Dashboard with dynamically loaded user details.
- Logout functionality.
- HTML, CSS and JavaScript frontend served through Spring Boot.

## Tech Stack

| Category        | Technologies               |
| --------------- | -------------------------- |
| Language        | Java 17                    |
| Backend         | Spring Boot                |
| Security        | Spring Security, JWT       |
| ORM             | Spring Data JPA, Hibernate |
| Database        | MySQL                      |
| Frontend        | HTML, CSS, JavaScript      |
| Build Tool      | Maven                      |
| API Testing     | Postman                    |
| Version Control | Git, GitHub                |

## Project Architecture

The application follows a layered architecture:

```text
src/main/java/com/portfolio/portfolio_app
│
├── config
│   ├── SecurityConfig.java
│   └── JwtAuthenticationFilter.java
│
├── controller
│   ├── AuthController.java
│   ├── UserController.java
│   ├── PortfolioController.java
│   └── TransactionController.java
│
├── dto
│   └── LoginRequest
```
