package org.example.ledgerapp.util;

import ch.qos.logback.core.util.StringUtil;
import org.example.ledgerapp.domain.dto.DoubleEntryDTO;
import org.example.ledgerapp.domain.dto.EntryDTO;
import org.example.ledgerapp.domain.dto.TransactionDTO;
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


}
