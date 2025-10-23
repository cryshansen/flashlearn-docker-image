package com.artog.flashlearn.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

import com.artog.flashlearn.config.OpenAiConfig;
import com.artog.flashlearn.dto.*;
import com.artog.flashlearn.util.FlashcardParser;

/**
 * 
 * This class manages the text flashcards generation based on topic
 * 
 * */

@Service
public class OpenAiService {

	private final OpenAiConfig config;
	private final RestTemplate restTemplate = new RestTemplate();
	
	public OpenAiService(OpenAiConfig config) {
		this.config = config;
	}
	

    public List<FlashcardDto> generateFlashcards(String topic) {
        String apiKey = config.getApiKey();
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
            "model", "gpt-4o-mini",
            "messages", List.of(
            	    Map.of("role", "system", "content", "You are a study flashcard generator. Always respond in strict JSON."),
            		/*Map.of("role", "user", "content", "Generate 10 flashcards (JSON) from easy,medium,hard  for topic: " + topic)*/
            		Map.of("role", "user", "content",
            	            """
            	            Generate 20 flashcards for the topic: %s 
            	            
            	             Rules:
				            - Respond ONLY as a JSON array.
				            - Each object must have: topic, question, answer, difficulty, color.
				            - difficulty: easy | medium | hard
				            - color mapping: easy=success, medium=warning, hard=danger
				            - Use memory cues:
				              * Wrap <strong>concepts or terms</strong> in <strong> tags
				              * Wrap <u>verbs, processes, behaviors</u> in <u> tags
				              * Wrap <em>important phrases</em> in <em> tags
				            - Example:
				              {
				         ÷      "topic": "Core Java",
				                "question": "What is functional programming in Java?",
				                "answer": "It <u>emphasizes</u> <strong>immutability</strong> and <em>stateless behavior</em>.",
				                "difficulty": "medium",
				                "color": "warning"
				              }
            	            """.formatted(topic)
            	        )
            		)
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        // Parse JSON response into FlashcardDto list (Jackson/Gson)
        return FlashcardParser.parse(response.getBody());
    }
    
    
}
