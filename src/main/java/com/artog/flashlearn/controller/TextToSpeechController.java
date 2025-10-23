package com.artog.flashlearn.controller;

import com.artog.flashlearn.dto.ProviderInfo;
import com.artog.flashlearn.service.TtsService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


import java.util.*;


@RestController
@RequestMapping("/api/tts")
public class TextToSpeechController {


    private final TtsService ttsService;  // means can not change the methods or pointer to the object
    
    @Autowired
    private ApplicationContext ctx;


    // Mock: In real app → load from DB / subscription manager
    private static final Map<String, ProviderInfo> providers = Map.of(
            "OpenAI", new ProviderInfo("OpenAI", true, "High-quality natural voice"),
            "Google", new ProviderInfo("Google", false, "Cost effective (~$4/1M chars)"),
            "AWS Polly", new ProviderInfo("AWS Polly", false, "Enterprise grade, many voices")
    );

    
    public TextToSpeechController( TtsService ttsService) {
        this.ttsService = ttsService;
    }
    
    
    
    
    @PostMapping(value = "/{provider}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> synthesizeSpeech(@PathVariable String provider,
                                                   @RequestBody String text,
                                                   HttpServletRequest request) {
    	
    	String tier = (String) request.getAttribute("subscriptionTier");
        if (tier == null) tier = "FREE";

        // Define rules
        boolean allowed = switch (tier) {
            case "FREE" -> provider.equals("OpenAI");
            case "MIDDLE" -> provider.equals("OpenAI") || provider.equals("Google");
            case "TOP" -> provider.equals("OpenAI") || provider.equals("Google") || provider.equals("AWS Polly");
            default -> false;
        };

        if (!allowed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(("Provider not available for tier: " + tier).getBytes());
        }

        
        try {
            ProviderInfo info = providers.get(provider);
            if (info == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(("Unknown provider: " + provider).getBytes());
            }

            if (!info.isActive()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(("Provider not active: " + provider).getBytes());
            }

            // Map provider name → Spring bean name
            String beanName = switch (provider) {
                case "OpenAI" -> "openAiTtsService";
                case "Google" -> "googleTtsService";
                case "AWS Polly" -> "awsPollyTtsService";
                default -> throw new IllegalArgumentException("Unsupported provider");
            };

            TtsService ttsService = (TtsService) ctx.getBean(beanName);
            byte[] audioBytes = ttsService.synthesize(text);

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=output.mp3");
            headers.set(HttpHeaders.CONTENT_TYPE, "audio/mpeg");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(audioBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(("Error: " + e.getMessage()).getBytes());
        }
    }
    
    
/*
    @PostMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> synthesizeSpeech(@RequestBody String text) {
        try {
            byte[] audioBytes = ttsService.synthesize(text);

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=output.mp3");
            headers.set(HttpHeaders.CONTENT_TYPE, "audio/mpeg");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(audioBytes);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    */
	
}
