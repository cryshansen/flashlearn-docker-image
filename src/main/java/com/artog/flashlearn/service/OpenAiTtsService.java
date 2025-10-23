package com.artog.flashlearn.service;

import com.artog.flashlearn.config.OpenAiConfig;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.util.Map;

@Service("openAiTtsService")
public class OpenAiTtsService {

    private static final String OPENAI_TTS_URL = "https://api.openai.com/v1/audio/speech";

    private final OpenAiConfig config;

    public OpenAiTtsService(OpenAiConfig config) {
        this.config = config;
    }

    public String synthesizeToFile(String text,String filename) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini-tts",
                "voice", "alloy",
                "input", text,
                "format", "mp3"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(config.getApiKey()); // <-- make sure OpenAiConfig returns the actual API key

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                OPENAI_TTS_URL, HttpMethod.POST, request, byte[].class
        );

        byte[] audioBytes = response.getBody();

        String outputPath = filename + "-output.mp3";
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(audioBytes);
        }

        return outputPath;
    }

	public String generateAndSaveAudio(String answer) {
		// TODO Auto-generated method stub
		return null;
	}
	public byte[] synthesize(String text) throws Exception {
	    RestTemplate restTemplate = new RestTemplate();

	    Map<String, Object> body = Map.of(
	            "model", "gpt-4o-mini-tts",
	            "voice", "alloy",
	            "input", text,
	            "format", "mp3"
	    );

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(config.getApiKey());

	    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

	    ResponseEntity<byte[]> response = restTemplate.exchange(
	            OPENAI_TTS_URL, HttpMethod.POST, request, byte[].class
	    );

	    return response.getBody(); // 👈 caller handles persistence
	}

    
    
}
