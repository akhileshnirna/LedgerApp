package org.example.ledgerapp.dto;

import org.example.ledgerapp.enums.AccountType;
import org.example.ledgerapp.enums.TransactionType;

import java.math.BigDecimal;

public class EntryDTO {
    private String timestamp;
    private String description;
    private BigDecimal amount;
    private AccountType accountType;
    private TransactionType transactionType;

    public EntryDTO() {
    }

    public EntryDTO(String timestamp, String description, BigDecimal amount, AccountType accountType, TransactionType transactionType) {
        this.timestamp = timestamp;
        this.description = description;
        this.amount = amount;
        this.accountType = accountType;
        this.transactionType = transactionType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
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

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

}


