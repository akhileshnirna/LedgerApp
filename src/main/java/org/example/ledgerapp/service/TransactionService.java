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
                LocalDateTime.parse(entryDTO.getTimestamp()));

    }

    public void setLedgerRepository(LedgerRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

}
