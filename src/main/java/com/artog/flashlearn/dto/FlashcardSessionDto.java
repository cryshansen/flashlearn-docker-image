package com.artog.flashlearn.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.artog.flashlearn.model.Flashcard;

public class FlashcardSessionDto {
    private Long sessionId;
    private String topic;
    private LocalDateTime createdAt;
    private List<Flashcard> flashcards;

    public FlashcardSessionDto(Long sessionId, String topic, LocalDateTime createdAt, List<Flashcard> flashcards) {
        this.sessionId = sessionId;
        this.topic = topic;
        this.createdAt = createdAt;
        this.flashcards = flashcards;
    }

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<Flashcard> getFlashcards() {
		return flashcards;
	}

	public void setFlashcards(List<Flashcard> flashcards) {
		this.flashcards = flashcards;
	}

}
