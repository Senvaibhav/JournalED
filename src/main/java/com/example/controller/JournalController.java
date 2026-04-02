package com.example.controller;

import com.example.entity.JournalEntry;
import com.example.entity.User;
import com.example.service.JournalEntryService;
import com.example.service.UserService;
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
    private final UserService userService;

    public JournalController(JournalEntryService journalEntryService, UserService userService) {
        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }

    // Get all entries
    @GetMapping("/{username}")
    public ResponseEntity<List<JournalEntry>> getAllJournalEntryOfUser(@PathVariable String username) {

        User user = userService.findByUserName(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<JournalEntry> entries = user.getJournalEntries();

        if (entries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(entries);
    }

    // Create new entry
    @PostMapping("{username}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry,@PathVariable String username) {
        myEntry.setDate(LocalDateTime.now());


         journalEntryService.saveEntry(myEntry,username);

        return new ResponseEntity<>(myEntry,HttpStatus.CREATED);
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
    @DeleteMapping("/id/{userName}/{id}")
    public ResponseEntity<String> deleteJournalEntry(@PathVariable ObjectId id,@PathVariable String userName) {

        Optional<JournalEntry> entry = journalEntryService.findById(id);

        if (entry.isPresent()) {
            journalEntryService.deleteById(id,userName);
            return ResponseEntity.ok("Entry deleted successfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Entry not found");
    }

    // Update entry

    @PutMapping("/id/{userName}/{id}")
    public ResponseEntity<JournalEntry> updateJournalEntry(
            @PathVariable ObjectId id,
            @RequestBody JournalEntry myEntry,
            @PathVariable String userName) {

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