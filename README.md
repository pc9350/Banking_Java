# Banking API

A simple Spring Boot REST API for a basic banking application. This project demonstrates core Spring Boot concepts with a clean, minimal implementation.

## Features

- Account management (create and view accounts)
- Transaction processing (deposits and withdrawals)
- Transaction history viewing
- In-memory H2 database with sample data

## Technologies Used

- Spring Boot 2.7.18
- Spring Data JPA
- H2 In-memory Database
- Java 11
- Maven

## Project Structure

``` 
banking-api/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/
│ │ │ └── example/
│ │ │ └── bankingapi/
│ │ │ ├── BankingApiApplication.java
│ │ │ ├── controller/
│ │ │ │ └── AccountController.java
│ │ │ ├── model/
│ │ │ │ ├── Account.java
│ │ │ │ └── Transaction.java
│ │ │ ├── repository/
│ │ │ │ ├── AccountRepository.java
│ │ │ │ └── TransactionRepository.java
│ │ │ └── service/
│ │ │ └── AccountService.java
│ │ └── resources/
│ │ ├── application.properties
│ │ └── data.sql
│ └── test/
│ └── java/
│ └── com/
│ └── example/
│ └── bankingapi/
│ └── service/
│ └── AccountServiceTest.java
└── pom.xml
```

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven

### Running the Application

1. Clone the repository
   ```
   git clone https://github.com/yourusername/banking-api.git
   cd banking-api
   ```

2. Build the project
   ```
   mvn clean install
   ```

3. Run the application
   ```
   mvn spring-boot:run
   ```

The application will start on port 8080.

### Accessing the H2 Database Console

The H2 console is available at: http://localhost:8080/h2-console

Connection details:
- JDBC URL: `jdbc:h2:mem:bankingdb`
- Username: `sa`
- Password: (leave empty)

## API Endpoints

### Accounts

- `GET /api/accounts` - Get all accounts
- `GET /api/accounts/{id}` - Get account by ID
- `POST /api/accounts` - Create a new account

Example request body for creating an account:

```json
{
  "name": "John Doe",
  "balance": 1000.00
}
```

### Transactions

- `POST /api/accounts/{id}/deposit` - Deposit money
- `POST /api/accounts/{id}/withdraw` - Withdraw money
- `GET /api/accounts/{id}/transactions` - Get transaction history

Example request body for deposit/withdraw:

```json
{
  "amount": 100.00
}
```


## Testing

### Using cURL

1. Get all accounts:
   ```
   curl -X GET http://localhost:8080/api/accounts
   ```

2. Create a new account:
   ```
   curl -X POST http://localhost:8080/api/accounts -H "Content-Type: application/json" -d "{\"name\":\"New Customer\",\"balance\":100.00}"
   ```

3. Deposit money:
   ```
   curl -X POST http://localhost:8080/api/accounts/1/deposit -H "Content-Type: application/json" -d "{\"amount\":50.00}"
   ```

4. Withdraw money:
   ```
   curl -X POST http://localhost:8080/api/accounts/1/withdraw -H "Content-Type: application/json" -d "{\"amount\":25.00}"
   ```

5. View transaction history:
   ```
   curl -X GET http://localhost:8080/api/accounts/1/transactions
   ```

### Running Unit Tests

Run the tests with:
   ```
   mvn test
   ```


## Project Highlights

- **Clean Architecture**: Separation of concerns with controller, service, and repository layers
- **RESTful API Design**: Following REST principles for resource management
- **Transaction Management**: Proper handling of financial transactions with appropriate error handling
- **Unit Testing**: Comprehensive tests for the service layer
- **In-Memory Database**: H2 database for easy setup and testing

## License

This project is licensed under the MIT License - see the LICENSE file for details.