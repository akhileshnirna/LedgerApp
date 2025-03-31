package org.example.ledgerapp.repository;

import org.example.ledgerapp.domain.Entry;
import org.example.ledgerapp.enums.AccountType;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LedgerRepository {

    private final List<Entry> entries = new ArrayList<>();
    private final Map<AccountType, List<Entry>> accountEntries = new HashMap<>();
    private final Map<AccountType, BigDecimal> accountBalance = new HashMap<>();

    public void recordEntry(Entry entry) {
        this.entries.add(entry);
        this.accountEntries.computeIfAbsent(entry.getAccountType(), k -> new ArrayList<>()).add(entry);
    }

    public void removeEntry(Entry entry) {
        this.entries.remove(entry);
        this.accountEntries.get(entry.getAccountType()).remove(entry);
    }

    public List<Entry> getEntriesByAccountType(AccountType accountType) {
        return this.accountEntries.getOrDefault(accountType, new ArrayList<>());
    }

    public void updateBalance(AccountType accountType, BigDecimal amount) {
        accountBalance.put(accountType, amount);
    }

    public BigDecimal getBalance(AccountType accountType) {
        return this.accountBalance.getOrDefault(accountType, BigDecimal.ZERO);
    }

    public List<Entry> getEntries() {
        return entries;
    }

}
