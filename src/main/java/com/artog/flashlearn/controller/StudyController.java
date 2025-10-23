package com.artog.flashlearn.controller;

import java.util.List;
import java.util.Map;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;

import com.artog.flashlearn.dto.StudySessionDto;
import com.artog.flashlearn.model.*;
import com.artog.flashlearn.service.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

/**
 * 
 * StudyController = AI flashcards
* 
* 
 * */
@RestController
@RequestMapping("/api/study")
@CrossOrigin(origins = "http:localhost:4200")
public class StudyController {


    private final StudyService studyService;
    private final FlashcardService flashcardService;
    public StudyController(StudyService studyService,FlashcardService flashcardService) {
    	this.studyService = studyService;
    	this.flashcardService = flashcardService;
    }

    @PostMapping(value = "/generate" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudySessionDto> generateStudyGuide(
            @RequestBody Map<String, String> payload,
            @AuthenticationPrincipal User user) {

        String topic = payload.get("topic");
        StudySessionDto session = studyService.generateStudyGuide(user, topic);
        
        return ResponseEntity.ok(session);
    }

    // ✅ GET /api/sessions/user/{userId}
    @GetMapping("/user/{userId}")
    public List<StudySession> getUserSessions(@PathVariable Long userId) {
        return studyService.getSessionsByUser(userId);
    }
 // ✅ GET /api/sessions/{id}/flashcards
    @GetMapping("/{id}/flashcards")
    public List<Flashcard> getSessionFlashcards(@PathVariable Long id) {
        return flashcardService.getFlashcardsBySession(id);
    }
    /**
     * 🔹 Trigger async backfill of missing flashcard audio.
     * Returns immediately while job runs in background.
     */
    @PutMapping("/backfill-audio")
    public ResponseEntity<String> backfillAudio() {
        studyService.backfillMissingAudio();
        return ResponseEntity.ok("🌀 Audio backfill started. Check logs for progress.");
    }
    
    @PutMapping("/session/{sessionId}/backfill-audio")
    public ResponseEntity<String> backfillAudioBySession(@PathVariable Long sessionId) {
        studyService.backfillMissingAudioBySession(sessionId);
        return ResponseEntity.ok("🌀 Audio backfill started for session " + sessionId);
    }

}