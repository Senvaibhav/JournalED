package com.example.service;

import com.example.entity.JournalEntry;
import com.example.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;

    public JournalEntryService(JournalEntryRepository journalEntryRepository) {
        this.journalEntryRepository = journalEntryRepository;
    }

    // Save or Update
    public JournalEntry saveEntry(JournalEntry journalEntry) {
        return journalEntryRepository.save(journalEntry);
    }

    // Get all entries
    public List<JournalEntry> getAllEntries() {
        return journalEntryRepository.findAll();
    }

    // Find by ID
    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    // Delete by ID
    public void deleteById(ObjectId id) {
        journalEntryRepository.deleteById(id);
    }
}