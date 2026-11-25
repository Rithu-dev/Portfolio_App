# Stock Portfolio Management System

A full-stack, multi-user Stock Portfolio Management application built using **Java 17, Spring Boot, Spring Security, JWT Authentication, MySQL, and HTML/CSS/JavaScript**.

The application enables users to securely manage their stock portfolios, execute BUY/SELL transactions, track holdings, and maintain transaction history. It implements transaction processing logic, user-level data authorization, and third-party financial API integration.

## Features

### Authentication & Security
- User registration with password encryption using BCrypt.
- User login with JWT-based authentication.
- Stateless authentication using Spring Security.
- Protected REST APIs using JWT authentication filters.
- User-level authorization to restrict access to individual portfolios and transactions.
- Current authenticated user identification through JWT.

### Portfolio Management
- Create and manage user-specific stock holdings.
- Retrieve portfolio holdings based on the authenticated user.
- Generate portfolio summaries, including total shares and investment value.
- Track stock quantities and average purchase prices.
- Maintain portfolio data with MySQL persistence.

### Transaction Management
- Implemented BUY and SELL transaction workflows.
- Maintain transaction history with timestamps.
- Calculate weighted average purchase price when additional shares are purchased.
- Automatically update portfolio quantities after transactions.
- Prevent selling more shares than currently owned.
- Automatically remove a holding when its quantity reaches zero.
- Associate transactions with the authenticated user instead of relying on client-supplied user IDs.

### Market Data Integration
- Integrated a third-party financial market data API using Spring's RestClient.
- Retrieve stock prices using stock symbols.
- Handle invalid symbols, provider errors, and API communication failures.

### Frontend
- User registration and login pages.
- Dashboard displaying dynamically loaded portfolio and user information.
- Separate dashboard sections for holdings, transactions, BUY, and SELL operations.
- JWT-based API communication from the frontend.
- Logout functionality.
- Responsive interface built using HTML, CSS, and JavaScript.

### Validation & Exception Handling
- Request validation using Jakarta Bean Validation.
- Business-rule validation for stock transactions.
- Centralized exception handling for application errors.
- Input validation for stock symbols and transaction details.

## Tech Stack

| Category | Technologies |
|---|---|
| Language | Java 17 |
| Backend | Spring Boot |
| Security | Spring Security, JWT |
| ORM | Spring Data JPA, Hibernate |
| Database | MySQL |
| API Integration | Spring RestClient, Twelve Data API |
| Frontend | HTML, CSS, JavaScript |
| Build Tool | Maven |
| API Testing | Postman |
| Version Control | Git, GitHub |

## Project Architecture

The application follows a layered architecture, separating API endpoints, business logic, data access, and persistence.

```text
src/main/java/com/portfolio/portfolio_app
│
├── config
│   ├── SecurityConfig.java
│   ├── JwtAuthenticationFilter.java
│   └── MarketDataConfig.java
│
├── controller
│   ├── AuthController.java
│   ├── UserController.java
│   ├── PortfolioController.java
│   ├── TransactionController.java
│   └── MarketDataController.java
│
├── dto
│   ├── LoginRequest.java
│   ├── TransactionRequest.java
│   ├── PortfolioSummaryDTO.java
│   └── StockPriceResponse.java
│
├── entity
│   ├── User.java
│   ├── Portfolio.java
│   └── Transaction.java
│
├── repository
│   ├── UserRepository.java
│   ├── PortfolioRepository.java
│   └── TransactionRepository.java
│
├── service
│   ├── CurrentUserService.java
│   ├── CustomUserDetailsService.java
│   ├── JwtService.java
│   ├── PortfolioService.java
│   ├── TransactionService.java
│   └── MarketDataService.java
│
└── PortfolioAppApplication.java

src/main/resources
│
├── static
│   ├── index.html
│   ├── login.html
│   ├── register.html
│   ├── css
│   │   └── style.css
│   └── js
│       └── app.js
│
└── application.properties
```

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/users` | Register a new user |
| POST | `/auth/login` | Authenticate user and generate JWT |
| GET | `/users/me` | Retrieve authenticated user details |
| POST | `/portfolio` | Add a portfolio holding |
| GET | `/portfolio/user/{userId}` | Retrieve user-specific holdings |
| GET | `/portfolio/user/{userId}/summary` | Retrieve portfolio summary |
| POST | `/transactions` | Create a BUY/SELL transaction |
| GET | `/transactions/user/{userId}` | Retrieve transaction history |
| GET | `/market/price?symbol=AAPL` | Retrieve stock price from market data provider |

**Note:** Protected endpoints require a valid JWT in the Authorization header.

## Configuration

Configure your MySQL database and JWT settings in `application.properties`.

Set the Twelve Data API key as an environment variable:

```text
TWELVE_DATA_API_KEY=your_api_key
```

Do not commit actual database passwords, JWT secrets, or API keys to version control.

## Running the Application

1. Clone the repository.
2. Configure the MySQL database.
3. Configure the required application properties and environment variables.
4. Build the application using Maven:

```bash
mvn clean install
```

5. Start the Spring Boot application:

```bash
mvn spring-boot:run
```

6. Open the application in your browser:

```text
http://localhost:8080
```

## Future Enhancements

- Integrate live market prices into dashboard portfolio valuation.
- Calculate real-time unrealized profit/loss and investment returns.
- Implement market data caching to reduce external API calls.
- Introduce database schema versioning using Flyway.
- Add automated unit and integration testing.
- Explore Kafka-based event processing and stock price alerts.
