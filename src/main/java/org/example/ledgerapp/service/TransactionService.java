package org.example.ledgerapp.service;

import org.example.ledgerapp.dto.DoubleEntryDTO;
import org.example.ledgerapp.domain.Entry;
import org.example.ledgerapp.dto.EntryDTO;
import org.example.ledgerapp.dto.TransactionDTO;
import org.example.ledgerapp.enums.AccountType;
import org.example.ledgerapp.repository.LedgerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private LedgerRepository ledgerRepository;

    public void rollback(AccountType accountType) {
        final List<Entry> accountEntries = ledgerRepository.getEntriesByAccountType(accountType);
        Entry latestEntry = accountEntries.get(accountEntries.size() - 1);
        List<Entry> latestCorrespondingAccountEntries = ledgerRepository.getEntriesByAccountType(latestEntry.getReferenceAccountType());
        Entry latestCorrespondingEntry = latestCorrespondingAccountEntries.get(latestCorrespondingAccountEntries.size() - 1);
        if (AccountType.isIncrease(latestEntry.getAccountType(), latestEntry.getTransactionType())) {
            ledgerRepository.updateBalance(latestEntry.getAccountType(), ledgerRepository.getBalance(latestEntry.getAccountType()).subtract(latestEntry.getAmount()));
            ledgerRepository.updateBalance(latestEntry.getReferenceAccountType(), ledgerRepository.getBalance(latestEntry.getReferenceAccountType()).subtract(latestCorrespondingEntry.getAmount()));
        } else {
            ledgerRepository.updateBalance(latestEntry.getAccountType(), ledgerRepository.getBalance(latestEntry.getAccountType()).add(latestEntry.getAmount()));
            ledgerRepository.updateBalance(latestEntry.getReferenceAccountType(), ledgerRepository.getBalance(latestEntry.getReferenceAccountType()).add(latestCorrespondingEntry.getAmount()));
        }
        ledgerRepository.removeEntry(latestEntry);
        ledgerRepository.removeEntry(latestCorrespondingEntry);
    }

    public void recordTransaction(TransactionDTO transaction) {
        for (DoubleEntryDTO entry : transaction.getEntries()) {
            this.handleEntry(entry.getEntry1());
            this.handleEntry(entry.getEntry2());
        }
    }

    public List<Entry> getTransactionHistory(LocalDateTime fromDate, LocalDateTime toDate) {
        final List<Entry> transactions = ledgerRepository.getEntries();
        return transactions.stream()
                .filter(entry -> {
                    boolean afterFrom = fromDate == null || !entry.getTimestamp().isBefore(fromDate);
                    boolean beforeTo = toDate == null || !entry.getTimestamp().isAfter(toDate);
                    return afterFrom && beforeTo;
                })
                .collect(Collectors.toList());
    }

    public BigDecimal getBalance(AccountType accountType) {
        return ledgerRepository.getBalance(accountType);
    }


    private void handleEntry(EntryDTO entryDTO) {

        final BigDecimal balance = ledgerRepository.getBalance(entryDTO.getAccountType());
        if (AccountType.isIncrease(entryDTO.getAccountType(), entryDTO.getTransactionType())) {
            ledgerRepository.updateBalance(entryDTO.getAccountType(), balance.add(entryDTO.getAmount()));
        } else {
            ledgerRepository.updateBalance(entryDTO.getAccountType(), balance.subtract(entryDTO.getAmount()));
        }
        final Entry entry = makeEntry(entryDTO);
        ledgerRepository.recordEntry(entry);
    }


    private Entry makeEntry(EntryDTO entryDTO) {
        return new Entry(entryDTO.getDescription(),
                entryDTO.getAmount(),
                entryDTO.getAccountType(),
                entryDTO.getTransactionType(),
                LocalDateTime.parse(entryDTO.getTimestamp()),
                entryDTO.getReferenceAccountType());

    }

    public void setLedgerRepository(LedgerRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

}
