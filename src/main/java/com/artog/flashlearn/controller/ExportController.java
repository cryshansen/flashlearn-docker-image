package com.artog.flashlearn.controller;

import java.util.Map;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.artog.flashlearn.model.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;



import com.artog.flashlearn.repo.FlashcardRepository;


@RestController
@RequestMapping("/api/export")
public class ExportController {
	
	private final FlashcardRepository flashcardRepository;

    public ExportController(FlashcardRepository flashcardRepository) {
        this.flashcardRepository = flashcardRepository;
    }
    
    
	@GetMapping("/export/json")
	public ResponseEntity<String> exportTopicJson() throws IOException {
	    List<Flashcard> flashcards = flashcardRepository.findAll();
	    Map<String, List<Map<String, Object>>> grouped = new HashMap<>();

	    for (Flashcard f : flashcards) {
	        String topic = f.getTopic();
	        grouped.putIfAbsent(topic, new ArrayList<>());

	        Map<String, Object> card = new HashMap<>();
	        card.put("topic", f.getTopic());
	        card.put("question", f.getQuestion());
	        card.put("answer", f.getAnswer());
	        card.put("difficulty", f.getDifficulty());
	        card.put("color", f.getColor());
	        card.put("question_audio_path", f.getQuestionAudioPath());
	        card.put("answer_audio_path", f.getAnswerAudioPath());
	        grouped.get(topic).add(card);
	    }

	    for (String topic : grouped.keySet()) {
	        Path out = Paths.get("export/" + topic + "-cards-data.json");
	        Files.createDirectories(out.getParent());
	        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(out.toFile(), grouped.get(topic));
	    }

	    return ResponseEntity.ok("Export complete");
	}
	@GetMapping("/export/topics")
	public ResponseEntity<String> exportFlashcards() throws IOException {
	    List<Flashcard> flashcards = flashcardRepository.findAll();

	    // Group flashcards by their session (not just topic)
	    Map<String, List<Map<String, Object>>> grouped = new HashMap<>();

	    for (Flashcard f : flashcards) {
	        String sessionTopic = f.getSession() != null ? f.getSession().getTopic() : "unknown";
	        grouped.putIfAbsent(sessionTopic, new ArrayList<>());

	        Map<String, Object> card = new HashMap<>();
	        card.put("topic", f.getTopic()); // subtopic or section within session
	        card.put("question", f.getQuestion());
	        card.put("answer", f.getAnswer());
	        card.put("difficulty", f.getDifficulty());
	        card.put("color", f.getColor());
	        card.put("question_audio_path", f.getQuestionAudioPath());
	        card.put("answer_audio_path", f.getAnswerAudioPath());
	        grouped.get(sessionTopic).add(card);
	    }

	    ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

	    for (String sessionTopic : grouped.keySet()) {
	        Path out = Paths.get("export/" + sessionTopic + "-cards-data.json");
	        Files.createDirectories(out.getParent());

	       // Map<String, Object> fileContent = new HashMap<>();
	       // fileContent.put("session", sessionTopic);
	       // fileContent.put("flashcards", grouped.get(sessionTopic));

	        mapper.writeValue(out.toFile(), grouped.get(sessionTopic));
	    }

	    return ResponseEntity.ok("✅ Export complete for " + grouped.size() + " sessions");
	}


}
