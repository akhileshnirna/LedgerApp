package org.example.ledgerapp.domain.dto;

public class DoubleEntryDTO {
    private EntryDTO entry1;
    private EntryDTO entry2;

    public DoubleEntryDTO() {
    }

    public DoubleEntryDTO(EntryDTO entry1, EntryDTO entry2) {
        this.entry1 = entry1;
        this.entry2 = entry2;
    }

    public EntryDTO getEntry1() {
        return entry1;
    }

    public EntryDTO getEntry2() {
        return entry2;
    }

    public void setEntry1(EntryDTO entry1) {
        this.entry1 = entry1;
    }

    public void setEntry2(EntryDTO entry2) {
        this.entry2 = entry2;
    }

}
