package org.example.ledgerapp.dto;

import java.util.List;

public class TransactionDTO {
    private List<DoubleEntryDTO> entries;

    public TransactionDTO() {
    }

    public TransactionDTO(List<DoubleEntryDTO> entries) {
        this.entries = entries;
    }

    public List<DoubleEntryDTO> getEntries() {
        return entries;
    }

    public void setEntries(List<DoubleEntryDTO> entries) {
        this.entries = entries;
    }
}
