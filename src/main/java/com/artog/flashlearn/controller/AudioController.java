package com.artog.flashlearn.controller;

import java.io.File;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.artog.flashlearn.model.Flashcard;
import com.artog.flashlearn.service.AudioService;


import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/api/audio")
public class AudioController {
	private final AudioService audioService;

    public AudioController(AudioService audioService) {
        this.audioService = audioService;
    }

    // 🔹 Generate audio if missing, then return it. This is deprecated as the requirement is both question and andswer whereas this does only one and was associated with the question originally
    //TODO refactor to do both audio files based on id and if the path field exists.
    @PutMapping("/flashcards/{id}")
    public ResponseEntity<Resource> generateFlashcardAudio(@PathVariable Long id) throws Exception {
        Flashcard flashcard = audioService.generateAudioIfMissing(id); //deprecated only facitilitates the one field values audio.

        if (flashcard.getQuestionAudioPath() == null) {
            return ResponseEntity.internalServerError().body(null);
        }

        File audioFile = new File(flashcard.getQuestionAudioPath());
        if (!audioFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(audioFile);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("audio/mpeg"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + audioFile.getName() + "\"")
                .body(resource);
    }
    
    @PutMapping("/flashcard/{id}/question")
    public ResponseEntity<Resource> generateQuestionAudio(@PathVariable Long id) {
        try {
            Flashcard flashcard = audioService.generateQuestionAudioIfMissing(id);
            
            if (flashcard.getQuestionAudioPath() == null) {
                return ResponseEntity.internalServerError().body(null);
            }
            
            File audioFile = new File(flashcard.getQuestionAudioPath());
            if (!audioFile.exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(audioFile);
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("audio/mpeg"))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + audioFile.getName() + "\"")
                    .body(resource);
            
            
            
            //return ResponseEntity.ok(flashcard);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/flashcard/{id}/answer")
    public ResponseEntity<Resource> generateAnswerAudio(@PathVariable Long id) {
        
        try {
            Flashcard flashcard = audioService.generateAnswerAudioIfMissing(id);
            
            if (flashcard.getAnswerAudioPath() == null) {
                return ResponseEntity.internalServerError().body(null);
            }
            
            File audioFile = new File(flashcard.getAnswerAudioPath());
            if (!audioFile.exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(audioFile);
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("audio/mpeg"))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + audioFile.getName() + "\"")
                    .body(resource);
            
            //return ResponseEntity.ok(flashcard);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        
        
        
        
    }
    @PostMapping("/slides")
    public ResponseEntity<?> buildSlidesAudio(@RequestParam String fileName) {
        return ResponseEntity.ok(audioService.generateSlidesAudio(fileName));
    }

    @PostMapping("/puzzles")
    public ResponseEntity<?> buildPuzzlesAudio(@RequestParam String fileName) {
        return ResponseEntity.ok(audioService.generatePuzzlesAudio(fileName));
    }

    @PostMapping("/pages")
    public ResponseEntity<?> buildPagesAudio(@RequestParam String fileName) {
        return ResponseEntity.ok(audioService.generatePagesAudio(fileName));
    }
    
    @PostMapping("/whiteboard")
    public ResponseEntity<?> buildWhiteboardAudio(@RequestParam String fileName) {
        return ResponseEntity.ok(audioService.generateWhiteBoardAudio(fileName));
    }
    
    @PostMapping("/interview")
    public ResponseEntity<?> buildInterviewAudio(@RequestParam String fileName) {
        return ResponseEntity.ok(audioService.generateInterviewAudio(fileName));
    }
    
    
    
}