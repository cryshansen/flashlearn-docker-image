package com.artog.flashlearn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tts")
public class TtsConfig {
    private String provider;
    private OpenAiConfig openai;
    private GoogleConfig google;
    private AwsConfig aws;
    
    // getters and setters

    public static class OpenAiConfig {
        private String apiKey;
        private String model;
        // getters/setters
    }
    public static class GoogleConfig {
        private String apiKey;
        private String voice;
        // getters/setters
    }
    public static class AwsConfig {
        private String accessKey;
        private String secretKey;
        private String region;
        private String voiceId;
        // getters/setters
    }
	public Object getOpenai() {
		// TODO Auto-generated method stub
		return null;
	}
}
