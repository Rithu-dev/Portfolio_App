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

| Category | Technologies |
|---|---|
| Language | Java 17 |
| Backend | Spring Boot |
| Security | Spring Security, JWT |
| ORM | Spring Data JPA, Hibernate |
| Database | MySQL |
| Frontend | HTML, CSS, JavaScript |
| Build Tool | Maven |
| API Testing | Postman |
| Version Control | Git, GitHub |

## Project Architecture

The application follows a **layered architecture**, separating configuration, controllers, business logic, database operations, and entities.

### Backend Structure

```text
src/main/java/com/portfolio/portfolio_app/
│
├── config/
│   ├── SecurityConfig.java
│   └── JwtAuthenticationFilter.java
│
├── controller/
│   ├── AuthController.java
│   ├── UserController.java
│   ├── PortfolioController.java
│   └── TransactionController.java
│
├── dto/
│   └── LoginRequest.java
│
├── entity/
│   ├── User.java
│   ├── Portfolio.java
│   └── Transaction.java
│
├── repository/
│
├── service/
│   ├── CurrentUserService.java
│   ├── CustomUserDetailsService.java
│   └── JwtService.java
│
└── exception/
```

### Frontend and Configuration

```text
src/main/resources/
│
├── static/
│   ├── index.html
│   ├── login.html
│   └── register.html
│
└── application.properties
```

## Getting Started

Follow these instructions to set up and run the application locally.

### Prerequisites

Make sure the following are installed:

- Java 17 or later
- Apache Maven
- MySQL Server
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/Rithu-dev/Portfolio_App.git
```

Navigate to the project directory:

```bash
cd Portfolio_App
```

### 2. Create the MySQL Database

Open MySQL and execute:

```sql
CREATE DATABASE portfolio_db;
```

### 3. Configure Application Properties

Open `src/main/resources/application.properties` and configure your local database credentials and JWT settings.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/portfolio_db
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

jwt.secret=YOUR_BASE64_SECRET
jwt.expiration=86400000
```

**Important:**
- Replace the database username and password with your local MySQL credentials.
- Generate a secure Base64-encoded JWT secret.
- Never commit actual database credentials or JWT secrets to GitHub.

### 4. Run the Application

Start the application using Maven:

```bash
mvn spring-boot:run
```

Once the application starts successfully, access it at:

**Application URL:** http://localhost:8080

**Registration:** http://localhost:8080/register.html

**Login:** http://localhost:8080/login.html

After registration, log in using your credentials to access the dashboard.
## API Endpoints

All protected endpoints require a valid JWT token in the request header:

```http
Authorization: Bearer <your_jwt_token>
```

### 1. Authentication

| Method | Endpoint | Description | Access |
|:---:|---|---|:---:|
| POST | `/users` | Register a new user | Public |
| POST | `/auth/login` | Authenticate user and generate JWT | Public |
| GET | `/users/me` | Retrieve authenticated user details | Protected |

### 2. User Management

| Method | Endpoint | Description | Access |
|:---:|---|---|:---:|
| GET | `/users` | Retrieve all users | Protected |
| GET | `/users/{id}` | Retrieve user by ID | Protected |
| PUT | `/users/{id}` | Update user details | Protected |
| DELETE | `/users/{id}` | Delete user by ID | Protected |

### 3. Portfolio Management

| Method | Endpoint | Description | Access |
|:---:|---|---|:---:|
| POST | `/portfolio` | Add a stock to the portfolio | Protected |
| GET | `/portfolio/user/{userId}` | Retrieve portfolio holdings | Protected |
| GET | `/portfolio/user/{userId}/summary` | Retrieve portfolio summary | Protected |

### 4. Transaction Management

| Method | Endpoint | Description | Access |
|:---:|---|---|:---:|
| POST | `/transactions` | Create a BUY/SELL transaction | Protected |
| GET | `/transactions/user/{userId}` | Retrieve transaction history | Protected |

## Security Implementation

- Password encryption using BCrypt.
- JWT-based authentication and authorization.
- Custom JWT authentication filter integrated with Spring Security.
- Stateless session management.
- Protected REST APIs.
- User-specific portfolio access control to prevent unauthorized access.

## Future Enhancements

- Integrate live stock market price APIs.
- Connect dashboard metrics and holdings to backend APIs.
- Complete frontend BUY/SELL transaction integration.
- Enhance transaction history and portfolio visualizations.
- Migrate monetary calculations from `double` to `BigDecimal`.
- Implement database versioning using Flyway.
- Add unit and integration tests.
- Containerize the application using Docker.

## Author

**Rithu**
- GitHub: [Rithu-dev](https://github.com/Rithu-dev)
- Project Repository: [Stock Portfolio Management System](https://github.com/Rithu-dev/Portfolio_App)

---

*Developed as a hands-on project to strengthen Java backend development skills and explore real-world application architecture.*
