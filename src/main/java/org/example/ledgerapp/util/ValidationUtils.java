package org.example.ledgerapp.util;

import ch.qos.logback.core.util.StringUtil;
import org.example.ledgerapp.dto.DoubleEntryDTO;
import org.example.ledgerapp.dto.EntryDTO;
import org.example.ledgerapp.dto.TransactionDTO;
import org.example.ledgerapp.enums.AccountType;
import org.example.ledgerapp.exception.InvalidAccountTypeException;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;

public class ValidationUtils {

    public static String validateTransaction(TransactionDTO transaction) {
        if (transaction == null || CollectionUtils.isEmpty(transaction.getEntries())) {
            return "Transaction or its entries should not be null";
        }

        for (int i = 0; i < transaction.getEntries().size(); i++) {
            DoubleEntryDTO entry = transaction.getEntries().get(i);

            if (entry == null || entry.getEntry1() == null || entry.getEntry2() == null) {
                return "One of the transaction entries is null";

            }

            EntryDTO entry1 = entry.getEntry1();
            EntryDTO entry2 = entry.getEntry2();

            if (StringUtil.isNullOrEmpty(entry1.getTimestamp()) || StringUtil.isNullOrEmpty(entry2.getTimestamp())) {
                return "Timestamp is missing from one of the transaction entries";

            }

            try {
                LocalDateTime debitTime = LocalDateTime.parse(entry1.getTimestamp());
                LocalDateTime creditTime = LocalDateTime.parse(entry2.getTimestamp());

                if (debitTime.isAfter(LocalDateTime.now()) || creditTime.isAfter(LocalDateTime.now())) {
                    return "An entry has a timestamp in the future";
                }
            } catch (Exception e) {
                return "An entry has an invalid timestamp";
            }

            if (entry1.getAmount() == null || entry2.getAmount() == null ||
                    entry1.getAmount().compareTo(entry2.getAmount()) != 0) {
                return "An entry has debit and credit amounts are missing or unequal";

            }

        }

        return null; // Valid
    }

    public static AccountType validateAccountType(String accountType) {
        if (StringUtil.isNullOrEmpty(accountType)) {
            throw new InvalidAccountTypeException("Account type should not be null or empty");
        }
        try {
            return AccountType.valueOf(accountType);
        } catch (IllegalArgumentException e) {
            throw new InvalidAccountTypeException("Invalid account type: " + accountType);
        }
    }

    public static void validateFromTo(LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("From date should be before to date");
        }
        if (from != null && from.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("From date should be in the past");
        }
        if (to != null && to.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("To date should be in the past");
        }
    }


}
