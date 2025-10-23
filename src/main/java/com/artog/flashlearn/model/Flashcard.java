package com.artog.flashlearn.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;
    private String question;

    @Column(length = 2000) // in case answers are long
    private String answer;

    private String difficulty;
    private String color;

    private String questionAudioPath;
    private String answerAudioPath;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    @JsonIgnore
    private StudySession session;

    public Flashcard() {}

    public Flashcard(StudySession session, String topic, String question, String answer, String difficulty, String color, String questionAudioPath,String answerAudioPath) {
        this.session = session;
        this.topic = topic;
        this.question = question;
        this.answer = answer;
        this.difficulty = difficulty;
        this.color = color;
        this.questionAudioPath = questionAudioPath;
        this.answerAudioPath = answerAudioPath;
    }

    public Flashcard(StudySession session, com.artog.flashlearn.dto.FlashcardDto dto) {
        this.session = session;
        this.topic = dto.getTopic();
        this.question = dto.getQuestion();
        this.answer = dto.getAnswer();
        this.difficulty = dto.getDifficulty();
        this.color = dto.getColor();
        this.questionAudioPath = dto.getQuestionAudioPath();
        this.answerAudioPath = dto.getAnswerAudioPath();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public StudySession getSession() {
		return session;
	}

	public void setSession(StudySession session) {
		this.session = session;
	}
	public String getQuestionAudioPath() {
		return questionAudioPath;
	}
	public void setQuestionAudioPath(String questionAudioPath) {
		this.questionAudioPath = questionAudioPath;
	}
	public String getAnswerAudioPath() {
		return answerAudioPath;
	}

	public void setAnswerAudioPath(String answerAudioPath) {
		this.answerAudioPath = answerAudioPath;
	}

    
}
