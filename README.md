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
- Accounts are only at the category level (i.e. no chart of accounts hierarchy)
- The application does not handle the accounting principles strictly.
- The application assumes that the transaction is atomic and does not handle partial transactions.

# Design
The application is designed using the following components:
- **Controller**: The controller layer is responsible for handling the incoming requests and returning the responses.
- **Service**: The service layer is responsible for handling the business logic.
- **Validator**: The validator layer is responsible for validating the incoming requests.
- **Repository**: The repository layer is responsible for handling the database operations.
  - In this case in-memory data structures are used to store the transactions and account balances.
- **Model**: The model layer contains the entities used in the application.
  

# Requirements
- Java 17
- Maven > 3.8

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