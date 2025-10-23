package com.artog.flashlearn.config;
import org.springframework.boot.context.properties.ConfigurationProperties;



@ConfigurationProperties(prefix = "openai")

public class OpenAiConfig {
	
	  private String apiKey;
	  private String chatModel;
	  private String ttsModel;
	  
	  /*
	   * TBD 
	   * public OpenAiConfig(String apiKey, String model, String ttsModel) {
		super();
		this.apiKey = apiKey;
		this.chatModel = model;
		this.ttsModel = ttsModel;
	  }*/
	  
	  public OpenAiConfig() {} // default constructor needed for @ConfigurationProperties
	  
	  
	  
	  public String getApiKey() {
		  return apiKey;
	  }
	  public void setApiKey(String apiKey) {
		  this.apiKey = apiKey;
	  }
	  public String getChatModel() {
		  return chatModel;
	  }
	  public void setChatModel(String model) {
		  this.chatModel = model;
	  }
	  public String getTtsModel() {
		  return ttsModel;
	  }
	  public void setTtsModel(String ttsModel) {
		  this.ttsModel = ttsModel;
	  }
	  
	  

}
