package com.example.controller;

import com.example.entity.JournalEntry;
import com.example.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalController {

    private final JournalEntryService journalEntryService;

    public JournalController(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
    }

    // Get all entries
    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAll() {
        List<JournalEntry> entries = journalEntryService.getAllEntries();

        if (entries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(entries);
    }

    // Create new entry
    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {
        myEntry.setDate(LocalDateTime.now());

        JournalEntry saved = journalEntryService.saveEntry(myEntry);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Get entry by ID
    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId id) {

        Optional<JournalEntry> entry = journalEntryService.findById(id);

        if (entry.isPresent()) {
            return ResponseEntity.ok(entry.get());
        }

        return ResponseEntity.notFound().build();
    }

    // Delete entry
    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteJournalEntry(@PathVariable ObjectId id) {

        Optional<JournalEntry> entry = journalEntryService.findById(id);

        if (entry.isPresent()) {
            journalEntryService.deleteById(id);
            return ResponseEntity.ok("Entry deleted successfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Entry not found");
    }

    // Update entry
    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntry> updateJournalEntry(
            @PathVariable ObjectId id,
            @RequestBody JournalEntry myEntry) {

        Optional<JournalEntry> entry = journalEntryService.findById(id);

        if (entry.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        JournalEntry old = entry.get();

        if (myEntry.getTitle() != null && !myEntry.getTitle().isEmpty()) {
            old.setTitle(myEntry.getTitle());
        }

        if (myEntry.getContent() != null && !myEntry.getContent().isEmpty()) {
            old.setContent(myEntry.getContent());
        }

        journalEntryService.saveEntry(old);

        return ResponseEntity.ok(old);
    }
}