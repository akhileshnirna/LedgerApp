package org.example.ledgerapp.domain;

import org.example.ledgerapp.enums.AccountType;
import org.example.ledgerapp.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Entry {
    private String description;
    private BigDecimal amount;
    private AccountType accountType;
    private TransactionType transactionType;
    private LocalDateTime timestamp;

    public Entry() {
    }

    public Entry(String description, BigDecimal amount, AccountType accountType, TransactionType transactionType, LocalDateTime timestamp) {
        this.description = description;
        this.amount = amount;
        this.accountType = accountType;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

}
