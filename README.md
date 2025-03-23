# Overview

This repository contains code for a simple Ledger Application.


# Implementation Details
The code assumes a double entry accounting system where every transaction has a debit and a credit entry. The accounting principles are only loosely followed in this application.
The account granularity is scoped only to the categorical level (ignores the charter of accounts hierarchy).

The table below explains how **debits** and **credits** affect the different account types in double-entry accounting:

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




