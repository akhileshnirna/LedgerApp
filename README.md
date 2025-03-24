# Overview
This repository contains code for a simple Ledger Application.

# Implementation Details
The application is a simple double-entry accounting system that records transactions and maintains account balances. The application follows the double-entry accounting system, which means that every transaction must have at least one debit and one credit entry.
The table below explains how **debits** and **credits** affect the different account types in double-entry accounting and the code follows the same principles.

| Account Type   | Debit Effect                         | Credit Effect                        |
|----------------|--------------------------------------|--------------------------------------|
| **ASSET**      | Increases the balance               | Decreases the balance               |
| **LIABILITY**  | Decreases the balance               | Increases the balance               |
| **EQUITY**     | Decreases the balance               | Increases the balance               |
| **REVENUE**    | Decreases the balance               | Increases the balance               |
| **EXPENSE**    | Increases the balance               | Decreases the balance               |


The application is a restful service that allows the following operations:
- Record multiple double-entry transactions
- Get the balance of a specific account
  - LIABILITY
  - ASSET
  - EQUITY
  - REVENUE
  - EXPENSE
- Get the transaction history.
    - optionally you can specify from and to dates

# Assumptions
- The application assumes that an external service would make calls to this service to record transactions.
- Only single currency supported
- The application allows negative balances for accounts. (i.e. overdrafts)
- Accounts are only at the category level (i.e. no chart of accounts hierarchy)
- The application may not handle the accounting principles strictly.
- The application assumes that the transaction is atomic and does not handle partial transactions.

# Design
The application is designed using the following components:
- **Controller**: The controller layer is responsible for handling the incoming requests and returning the responses.
- **Service**: The service layer is responsible for handling the business logic.
- **Validator**: The validator layer is responsible for validating the incoming requests.
- **Exception**: The exception layer is responsible for handling the exceptions.
- **Repository**: The repository layer is responsible for handling the database operations.
  - In this case in-memory data structures are used to store the transactions and account balances.
- **Model**: The model layer contains the entities used in the application.
  
# API Documentation
You can access the API using the following endpoint:
http://localhost:8080/swagger-ui/index.html

If you're using curl, you can use the following endpoint:
http://localhost:8080/v1/ledger

# API Endpoints
The application exposes the following endpoints:
- **POST /v1/ledger/transactions**: Record multiple double-entry transactions.
  - Request Body:
    ```json
      {
        "entries": [
          {
            "entry1": {
                "timestamp": "2024-03-23T16:00:00",
                "description": "Purchase of Equipment",
                "amount": 500,
                "accountType": "ASSET",
                "transactionType": "DEBIT"
            },
            "entry2": {
                "timestamp": "2024-03-23T16:00:00",
                "description": "Purchase of Equipment",
                "amount": 500,
                "accountType": "LIABILITY",
                "transactionType": "CREDIT"
            }
          },
          {
            "entry1": {
                "timestamp": "2024-03-24T09:30:00",
                "description": "Service Revenue",
                "amount": 300,
                "accountType": "EXPENSE",
                "transactionType": "DEBIT"
            },
            "entry2": {
                "timestamp": "2024-03-24T09:30:00",
                "description": "Service Revenue",
                "amount": 300,
                "accountType": "REVENUE",
                "transactionType": "CREDIT"
            }
          },
          {
            "entry1": {
                "timestamp": "2024-03-25T14:45:00",
                "description": "Loan Repayment",
                "amount": 200,
                "accountType": "LIABILITY",
                "transactionType": "DEBIT"
            },
            "entry2": {
                "timestamp": "2024-03-25T14:45:00",
                "description": "Loan Repayment",
                "amount": 200,
                "accountType": "ASSET",
                "transactionType": "CREDIT"
            }
          }
      ]
    }
    ```
  - Response Body:
    - 200 OK:
      "Transaction recorded successfully"
    - 400 Bad Request:
        Return different validation messages based on the validation error.

- **GET /v1/ledger/balance/{accountType}**: Get the balance of a specific account.
  - Request Parameters:
    - accountType: The account type for which the balance is required.
      - Possible values: ASSET, LIABILITY, EQUITY, REVENUE, EXPENSE
  - Response Body:
    - 200 OK:
      - The balance of the account type.
    - 400 Bad Request:
      - A message if the wrong account type is provided.
  - Example:
    - Request: '/v1/ledger/balance/ASSET'
    - Response: 200 OK
      - "balance": 300
- **GET /v1/ledger/history**: Get the transaction history.
  - Request Parameters:
    - from: The start date for the transaction history. [Optional]
      - format: Eg: 2024-03-23T16:00:00
    - to: The end date for the transaction history. [Optional]
      - format: Eg: 2024-03-23T16:00:00
  - Response Body:
    - 200 OK:
      - The transaction history based on the from and to dates.
      - If the from and to dates are not provided, return all transactions. (no pagination)
      - Example:
        - ```json
            [
                {
                    "timestamp": "2024-03-23T15:30:00",
                    "description": "TEST",
                    "amount": 100,
                    "accountType": "ASSET",
                    "transactionType": "DEBIT"
                },
                {
                    "timestamp": "2024-03-23T15:30:00",
                    "description": "TEST",
                    "amount": 100,
                    "accountType": "LIABILITY",
                    "transactionType": "CREDIT"
                }
            ]
          ```
    - 400 Bad Request:
      - If the from date is after the to date
      - If from date is after the current date
      - If to date is after the current date
      - If the date format is incorrect

# DRY RUN

### Posting Transactions
```bash
curl -X 'POST' \
  'http://localhost:8080/v1/ledger/transactions' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
    "entries": [
        {
            "entry1": {
                "timestamp": "2024-03-23T16:00:00",
                "description": "Purchase of Equipment",
                "amount": 500,
                "accountType": "ASSET",
                "transactionType": "DEBIT"
            },
            "entry2": {
                "timestamp": "2024-03-23T16:00:00",
                "description": "Purchase of Equipment",
                "amount": 500,
                "accountType": "LIABILITY",
                "transactionType": "CREDIT"
            }
        },
        {
            "entry1": {
                "timestamp": "2024-03-24T09:30:00",
                "description": "Service Revenue",
                "amount": 300,
                "accountType": "EXPENSE",
                "transactionType": "DEBIT"
            },
            "entry2": {
                "timestamp": "2024-03-24T09:30:00",
                "description": "Service Revenue",
                "amount": 300,
                "accountType": "REVENUE",
                "transactionType": "CREDIT"
            }
        },
        {
            "entry1": {
                "timestamp": "2024-03-25T14:45:00",
                "description": "Loan Repayment",
                "amount": 200,
                "accountType": "LIABILITY",
                "transactionType": "DEBIT"
            },
            "entry2": {
                "timestamp": "2024-03-25T14:45:00",
                "description": "Loan Repayment",
                "amount": 200,
                "accountType": "ASSET",
                "transactionType": "CREDIT"
            }
        }
    ]
}'
```

### Fetch ASSET account balance
```bash
curl -X 'GET' \
  'http://localhost:8080/v1/ledger/balance/ASSET' \
  -H 'accept: */*'
```

### Get Transaction History for dateTime = 2024-03-25T14:43:00
```bash
curl -X 'GET' \
  'http://localhost:8080/v1/ledger/history?from=2024-03-25T14%3A43%3A00' \
  -H 'accept: */*'
```

# Requirements
- Java 17
- Maven > 3.8
- Spring Boot

# How to run the application
Java 17 is required to run the application.

To run the application, execute the following command:

Clone the repository:
```bash 
https://github.com/akhileshnirna/LedgerApp.git
```

Navigate to the project directory:
```bash
cd LedgerApp
```

Run the application:
```bash
./mvnw spring-boot:run
```

The application will start on port 8080.

# Future Improvements
- Add support for multiple currencies.
- Add support for chart of accounts hierarchy.
- Add support for accounting principles.
- Add support for partial transactions.
- Add support for concurrent transactions.
- Add support for pagination in the transaction history.
- Provide more flexibility over searching for transactions based on different criteria.
- Adding integration tests.
- Extensive exception handling.