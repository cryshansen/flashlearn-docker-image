package com.artog.flashlearn.dto;

public class FlashcardDto {
    private Long id;
	private String question;
	private String answer;
	private String topic;
	private String difficulty;
	private String color;
	private String questionAudioPath;
	private String answerAudioPath;
	
	public FlashcardDto() {

	}
	
	 public FlashcardDto(String topic, String question, String answer, String difficulty, String color,String questionAudioPath, String answerAudioPath) {
	        this.topic = topic;
	        this.question = question;
	        this.answer = answer;
	        this.difficulty = difficulty;
	        this.color = color;
	        this.questionAudioPath = questionAudioPath;
	        this.answerAudioPath = answerAudioPath;
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
