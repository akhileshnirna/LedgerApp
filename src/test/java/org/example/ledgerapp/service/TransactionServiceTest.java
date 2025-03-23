package org.example.ledgerapp.service;

import org.example.ledgerapp.dto.DoubleEntryDTO;
import org.example.ledgerapp.dto.EntryDTO;
import org.example.ledgerapp.dto.TransactionDTO;
import org.example.ledgerapp.domain.enums.AccountType;
import org.example.ledgerapp.domain.enums.TransactionType;
import org.example.ledgerapp.repository.LedgerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionServiceTest {

    private final TransactionService transactionService = new TransactionService();
    private final LedgerRepository ledgerRepository = new LedgerRepository();

    @BeforeEach
    public void setUp() {
        this.transactionService.setLedgerRepository(ledgerRepository);
    }

    @Test
    void testRecordTransaction() {
        TransactionDTO transaction = new TransactionDTO();
        DoubleEntryDTO doubleEntryDTO = new DoubleEntryDTO();
        EntryDTO entry1 = makeEntry(BigDecimal.valueOf(100), "2021-01-01T00:00:00", AccountType.ASSET, TransactionType.DEBIT);
        EntryDTO entry2 = makeEntry(BigDecimal.valueOf(100), "2021-01-01T00:00:00", AccountType.LIABILITY, TransactionType.CREDIT);
        doubleEntryDTO.setEntry1(entry1);
        doubleEntryDTO.setEntry2(entry2);
        transaction.setEntries(List.of(doubleEntryDTO));
        transactionService.recordTransaction(transaction);

        assertEquals(BigDecimal.valueOf(100), transactionService.getBalance(AccountType.ASSET));
        assertEquals(BigDecimal.valueOf(100), transactionService.getBalance(AccountType.LIABILITY));
    }

    @Test
    void testRecordTransaction_multipleEntries() {
        TransactionDTO transaction = new TransactionDTO();
        DoubleEntryDTO doubleEntryDTO = new DoubleEntryDTO();
        EntryDTO entry1 = makeEntry(BigDecimal.valueOf(100), "2021-01-01T00:00:00", AccountType.ASSET, TransactionType.DEBIT);
        EntryDTO entry2 = makeEntry(BigDecimal.valueOf(100), "2021-01-01T00:00:00", AccountType.LIABILITY, TransactionType.CREDIT);
        doubleEntryDTO.setEntry1(entry1);
        doubleEntryDTO.setEntry2(entry2);

        DoubleEntryDTO doubleEntryDTO2 = new DoubleEntryDTO();
        EntryDTO entry3 = makeEntry(BigDecimal.valueOf(10), "2021-01-01T00:01:00", AccountType.ASSET, TransactionType.CREDIT);
        EntryDTO entry4 = makeEntry(BigDecimal.valueOf(10), "2021-01-01T00:01:00", AccountType.LIABILITY, TransactionType.DEBIT);
        doubleEntryDTO2.setEntry1(entry3);
        doubleEntryDTO2.setEntry2(entry4);

        transaction.setEntries(List.of(doubleEntryDTO, doubleEntryDTO2));
        transactionService.recordTransaction(transaction);

        assertEquals(BigDecimal.valueOf(90), transactionService.getBalance(AccountType.ASSET));
        assertEquals(BigDecimal.valueOf(90), transactionService.getBalance(AccountType.LIABILITY));
    }

    private EntryDTO makeEntry(BigDecimal amount, String dateTime, AccountType accountType, TransactionType transactionType) {
        return new EntryDTO(dateTime, "description", amount, accountType, transactionType);
    }

}
