package com.artog.flashlearn.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;


import com.artog.flashlearn.dto.FlashcardSessionDto;
import com.artog.flashlearn.model.*;
import com.artog.flashlearn.service.*;
/*
 * FlashcardController = Cruds of flashcards for user session engagements
 * */

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {

    @Autowired
    private FlashcardService flashcardService;

    /* this is for the adding manually a flashcard or set of flashcards manually using the textarea form */
    @PostMapping("/cues")
    public ResponseEntity<?> getCues(@RequestBody List<Flashcard> flashcards) {
        String cues = flashcardService.generateCues(flashcards);
        return ResponseEntity.ok(Map.of("cues", cues));
    }
    
 // Fetch flashcards by session id (or user)
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<Flashcard>> getFlashcardsBySession(@PathVariable Long sessionId) {
        List<Flashcard> flashcards = flashcardService.getFlashcardsBySession(sessionId);
        return ResponseEntity.ok(flashcards);
    }
   
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<FlashcardSessionDto>> getFlashcardsByUserId( @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)
    {

	    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
	    Page<FlashcardSessionDto> grouped = flashcardService.getFlashcardsGroupedBySession(userId, pageable);
	    return ResponseEntity.ok(grouped);
        
    }
    

    
    @GetMapping("/search")
    public ResponseEntity<Page<Flashcard>> searchFlashcards(
            @RequestParam Long userId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Flashcard> results = flashcardService.searchFlashcards(userId, keyword, page, size);
        return ResponseEntity.ok(results);
    }
    
    // todo integrate the two audio contents for both?
    @GetMapping("/{id}/audio")
    public ResponseEntity<byte[]> getFlashcardAudio(@PathVariable Long id) throws IOException {
        Flashcard flashcard = flashcardService.findById(id).orElseThrow(()-> new RuntimeException("Flashcard not found"));
        byte[] audio = Files.readAllBytes(Paths.get(flashcard.getAnswerAudioPath()));

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(audio);
    }
    
}
