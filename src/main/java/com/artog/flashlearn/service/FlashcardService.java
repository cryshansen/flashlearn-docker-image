package com.artog.flashlearn.service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.artog.flashlearn.config.OpenAiConfig;
import com.artog.flashlearn.dto.FlashcardSessionDto;
import com.artog.flashlearn.model.*;
import com.artog.flashlearn.repo.FlashcardRepository;
import com.artog.flashlearn.repo.StudySessionRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * This Service calls open ai to create flashcards from chatbot. it is a simplistic method and may switch to using as a flashcard crud process 
 * The file was generated as an example how to but the OpenAiService is the one wanted in production.
 * 
 * */

@Service
public class FlashcardService {

	private final OpenAiConfig config;
	
	@Autowired
    private FlashcardRepository flashcardRepository;
	private  StudySessionRepository sessionRepo;
	
	
	
    //@Value("${openai.apiKey}")
    //private String apiKey;

    public FlashcardService(OpenAiConfig config,StudySessionRepository sessionRepo) {
    	this.config = config;
    	this.sessionRepo = sessionRepo;
    }

    public String generateCues(List<Flashcard> flashcards) {
        OkHttpClient client = new OkHttpClient();

        String prompt = "Here are flashcards: " + flashcards.toString() +
                        "\nFor each, generate memory cues (mnemonics, visuals, keywords).";

        RequestBody body = RequestBody.create(
            MediaType.parse("application/json"),
            "{ \"model\": \"" + config.getChatModel() + "\", " +
            "\"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}] }"
        );

        Request request = new Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer " + config.getApiKey())
            .post(body)
            .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("Failed to call OpenAI API", e);
        }
    }
    
    // Fetch all flashcards for a given session
    public List<Flashcard> getFlashcardsBySession(Long sessionId) {
        return flashcardRepository.findBySession_Id(sessionId);
    }
    
    public List<Flashcard> getFlashcardsByUserId(Long userId) {
        return flashcardRepository.findBySession_User_Id(userId);
    }
    
    
    public Page<FlashcardSessionDto> getFlashcardsGroupedBySession(Long userId, Pageable pageable) {
        Page<StudySession> sessions = sessionRepo.findByUser_Id(userId, pageable);
  

        return sessions.map(session -> new FlashcardSessionDto(
                session.getId(),
                session.getTopic(),
                session.getCreatedAt(),
                session.getFlashcards()
        ));
    }

    public Page<Flashcard> searchFlashcards(Long userId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return flashcardRepository.searchByUserIdAndKeyword(userId, keyword, pageable);
    }

	public Optional<Flashcard> findById(Long id) {
		// TODO Auto-generated method stub
		  return flashcardRepository.findById(id); // because optional it errors with the else throw .orElseThrow(() -> new EntityNotFoundException("Flashcard not found with ID " + id));
	}
    

    
    
}
