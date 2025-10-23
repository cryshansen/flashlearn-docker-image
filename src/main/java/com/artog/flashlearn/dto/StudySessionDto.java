package com.artog.flashlearn.dto;

import java.util.List;

import com.artog.flashlearn.dto.*;
import com.artog.flashlearn.model.*;

import java.time.LocalDateTime;
import java.util.List;

public class StudySessionDto {
    private Long sessionId;
    private String topic;
    private String username;
    private Long userId;
    

	private LocalDateTime createdAt;
    private List<FlashcardDto> flashcards;

    public StudySessionDto() {}

    public StudySessionDto(Long sessionId, String topic, String username, LocalDateTime createdAt, List<FlashcardDto> flashcards) {
        this.sessionId = sessionId;
        this.topic = topic;
        this.username = username;
        this.createdAt = createdAt;
        this.flashcards = flashcards;
    }
    
    public StudySessionDto(Long sessionId, String topic, Long userid, LocalDateTime createdAt) {
        this.sessionId = sessionId;
        this.topic = topic;
        this.userId = userid;
        this.createdAt = createdAt;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<FlashcardDto> getFlashcards() {
		return flashcards;
	}

	public void setFlashcards(List<FlashcardDto> flashcards) {
		this.flashcards = flashcards;
	}

    
}
