package com.artog.flashlearn.util;

import com.artog.flashlearn.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public final class FlashcardParser {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<FlashcardDto> parse(String responseBody) {
        List<FlashcardDto> flashcards = new ArrayList<>();

        try {
        	
            JsonNode root = mapper.readTree(responseBody);
            JsonNode choices = root.path("choices");

            if (!choices.isArray() || choices.size() == 0) {
                throw new RuntimeException("OpenAI response contains no choices");
            }

            if (!choices.isArray() || choices.size() == 0) {
                throw new RuntimeException("OpenAI response contains no choices");
            }
            // Navigate OpenAI's response
            String content = choices.get(0).path("message").path("content").asText();
            content = stripFences(content);

            // Parse the cleaned JSON content (expecting an array)
            JsonNode flashcardArray = mapper.readTree(content);

            if (!flashcardArray.isArray()) {
                throw new RuntimeException("Expected JSON array of flashcards but got: " + flashcardArray.getNodeType());
            }
          
            for (JsonNode node : flashcardArray) {
                FlashcardDto dto = new FlashcardDto();
                dto.setTopic(node.path("topic").asText());
                dto.setQuestion(node.path("question").asText());
                dto.setAnswer(node.path("answer").asText());
                dto.setDifficulty(node.path("difficulty").asText());
                dto.setColor(node.path("color").asText());

                // Dev-friendly fallback: if memory cues missing, auto-wrap answer in <em>
                if (!MemoryCueValidator.isValid(dto.getAnswer())) {
                    dto.setAnswer("<em>" + dto.getAnswer() + "</em>");
                    System.out.println("⚠️ Memory cues missing — auto-wrapped answer for question: " + dto.getQuestion());
                }

                flashcards.add(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse flashcards", e);
        }

 
        return flashcards;
    }
    



    /**
     * Parse the OpenAI HTTP response body into a list of FlashcardDto.
     * - strips common markdown fences/backticks
     * - validates memory cues and auto-wraps missing cues (dev-friendly)
     */
   

    /** Remove triple-backtick fences, single backticks, and trim whitespace. */
    private static String stripFences(String s) {
        if (s == null) return "";
        String out = s.trim();

        // remove leading ``` or ```json
        out = out.replaceAll("(?s)^\\s*```(?:json)?\\s*", "");
        // remove trailing ```
        out = out.replaceAll("(?s)\\s*```\\s*$", "");
        // remove any remaining single backticks
        out = out.replace("`", "");

        return out.trim();
    }
}
