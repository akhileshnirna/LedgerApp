package org.example.ledgerapp.util;

import org.example.ledgerapp.dto.DoubleEntryDTO;
import org.example.ledgerapp.dto.EntryDTO;
import org.example.ledgerapp.dto.TransactionDTO;
import org.example.ledgerapp.domain.enums.AccountType;
import org.example.ledgerapp.domain.enums.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ValidationUtilTest {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal TEN = BigDecimal.valueOf(10);

    @Test
    void validateTransaction_validTransaction() {
        TransactionDTO transaction = new TransactionDTO();
        DoubleEntryDTO doubleEntryDTO = new DoubleEntryDTO();
        EntryDTO entry1 = makeEntry(HUNDRED, "2021-01-01T00:00:00", AccountType.ASSET, TransactionType.DEBIT);
        EntryDTO entry2 = makeEntry(HUNDRED, "2021-01-01T00:00:00", AccountType.LIABILITY, TransactionType.CREDIT);
        doubleEntryDTO.setEntry1(entry1);
        doubleEntryDTO.setEntry2(entry2);

        DoubleEntryDTO doubleEntryDTO2 = new DoubleEntryDTO();
        EntryDTO entry3 = makeEntry(TEN, "2021-01-01T00:01:00", AccountType.ASSET, TransactionType.CREDIT);
        EntryDTO entry4 = makeEntry(TEN, "2021-01-01T00:01:00", AccountType.LIABILITY, TransactionType.DEBIT);

        doubleEntryDTO2.setEntry1(entry3);
        doubleEntryDTO2.setEntry2(entry4);

        transaction.setEntries(List.of(doubleEntryDTO, doubleEntryDTO2));

        Assert.isNull(ValidationUtils.validateTransaction(transaction), "Transaction should be valid");
    }

    @Test
    void validateTransaction_nullTransaction() {
        Assert.isTrue(ValidationUtils.validateTransaction(null).equals("Transaction or its entries should not be null"), "Transaction should not be null");
    }

    @Test
    void validateTransaction_nullEntries() {
        TransactionDTO transaction = new TransactionDTO();
        Assert.isTrue(Objects.equals(ValidationUtils.validateTransaction(transaction), "Transaction or its entries should not be null"), "Transaction entries should not be null");
    }

    @Test
    void validateTransaction_nullTimestamp() {
        TransactionDTO transaction = new TransactionDTO();
        DoubleEntryDTO doubleEntryDTO = new DoubleEntryDTO();
        EntryDTO entry1 = makeEntry(HUNDRED, null, AccountType.ASSET, TransactionType.DEBIT);
        EntryDTO entry2 = makeEntry(HUNDRED, "2021-01-01T00:00:00", AccountType.LIABILITY, TransactionType.CREDIT);
        doubleEntryDTO.setEntry1(entry1);
        doubleEntryDTO.setEntry2(entry2);

        transaction.setEntries(List.of(doubleEntryDTO));
        Assert.isTrue(Objects.equals(ValidationUtils.validateTransaction(transaction), "Timestamp is missing from one of the transaction entries"), "Timestamp should not be null");
    }

    @Test
    void validateTransaction_invalidTimestamp() {
        TransactionDTO transaction = new TransactionDTO();
        DoubleEntryDTO doubleEntryDTO = new DoubleEntryDTO();
        EntryDTO entry1 = makeEntry(HUNDRED, "2021-01-01T00:000", AccountType.ASSET, TransactionType.DEBIT);
        EntryDTO entry2 = makeEntry(HUNDRED, "2021-01-01T00:00:00", AccountType.LIABILITY, TransactionType.CREDIT);
        doubleEntryDTO.setEntry1(entry1);
        doubleEntryDTO.setEntry2(entry2);

        transaction.setEntries(List.of(doubleEntryDTO));
        Assert.isTrue(Objects.equals(ValidationUtils.validateTransaction(transaction), "An entry has an invalid timestamp"), "Timestamp should be valid");
    }

    @Test
    void validateTransaction_futureTimestamp() {
        TransactionDTO transaction = new TransactionDTO();
        DoubleEntryDTO doubleEntryDTO = new DoubleEntryDTO();
        EntryDTO entry1 = makeEntry(HUNDRED, LocalDateTime.now().plusDays(1).toString(), AccountType.ASSET, TransactionType.DEBIT);
        EntryDTO entry2 = makeEntry(HUNDRED, LocalDateTime.now().plusDays(1).toString(), AccountType.LIABILITY, TransactionType.CREDIT);
        doubleEntryDTO.setEntry1(entry1);
        doubleEntryDTO.setEntry2(entry2);

        transaction.setEntries(List.of(doubleEntryDTO));
        Assert.isTrue(Objects.equals(ValidationUtils.validateTransaction(transaction), "An entry has a timestamp in the future"), "Timestamp should not be in the future");
    }

    @Test
    void validateTransaction_unequalAmounts() {
        TransactionDTO transaction = new TransactionDTO();
        DoubleEntryDTO doubleEntryDTO = new DoubleEntryDTO();
        EntryDTO entry1 = makeEntry(HUNDRED, "2021-01-01T00:00:00", AccountType.ASSET, TransactionType.DEBIT);
        EntryDTO entry2 = makeEntry(TEN, "2021-01-01T00:00:00", AccountType.LIABILITY, TransactionType.CREDIT);
        doubleEntryDTO.setEntry1(entry1);
        doubleEntryDTO.setEntry2(entry2);

        transaction.setEntries(List.of(doubleEntryDTO));
        Assert.isTrue(Objects.equals(ValidationUtils.validateTransaction(transaction), "An entry has debit and credit amounts are missing or unequal"), "Amounts should be equal");
    }

    @Test
    void validateTransaction_missingAmounts() {
        TransactionDTO transaction = new TransactionDTO();
        DoubleEntryDTO doubleEntryDTO = new DoubleEntryDTO();
        EntryDTO entry1 = makeEntry(HUNDRED, "2021-01-01T00:00:00", AccountType.ASSET, TransactionType.DEBIT);
        EntryDTO entry2 = makeEntry(null, "2021-01-01T00:00:00", AccountType.LIABILITY, TransactionType.CREDIT);
        doubleEntryDTO.setEntry1(entry1);
        doubleEntryDTO.setEntry2(entry2);

        transaction.setEntries(List.of(doubleEntryDTO));
        Assert.isTrue(Objects.equals(ValidationUtils.validateTransaction(transaction), "An entry has debit and credit amounts are missing or unequal"), "Amounts should not be null");
    }

    private EntryDTO makeEntry(BigDecimal amount, String dateTime, AccountType accountType, TransactionType transactionType) {
        return new EntryDTO(dateTime, "description", amount, accountType, transactionType);
    }

}
