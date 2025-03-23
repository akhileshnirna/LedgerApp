package org.example.ledgerapp.domain.enums;


public enum AccountType {
    ASSET,
    LIABILITY,
    EQUITY,
    REVENUE,
    EXPENSE,
    UNKNOWN;

    public static boolean isIncrease(AccountType accountType, TransactionType transactionType) {
        return switch (accountType) {
            case ASSET, EXPENSE -> TransactionType.DEBIT.equals(transactionType);
            case LIABILITY, EQUITY, REVENUE -> !TransactionType.DEBIT.equals(transactionType);
            default -> throw new IllegalArgumentException("Invalid account type");
        };
    }


}
