package com.artog.flashlearn.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.*;

import com.artog.flashlearn.dto.ProviderInfo;

import java.util.List;

/*
 * ProviderController = external providers (tier restrictions applied)
 * 
 * */


@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    @GetMapping
    public List<ProviderInfo> getProviders(HttpServletRequest request) {
    	
    	 String tier = (String) request.getAttribute("subscriptionTier");
    	    if (tier == null) tier = "FREE"; // fallback

    	    return switch (tier) {
    	        case "FREE" -> List.of(
    	                new ProviderInfo("OpenAI", true, "High-quality natural voice"),
    	                new ProviderInfo("Google", false, "Upgrade to Middle tier"),
    	                new ProviderInfo("AWS Polly", false, "Upgrade to Top tier")
    	        );
    	        case "MIDDLE" -> List.of(
    	                new ProviderInfo("OpenAI", true, "High-quality natural voice"),
    	                new ProviderInfo("Google", true, "Cost effective (~$4/1M chars)"),
    	                new ProviderInfo("AWS Polly", false, "Upgrade to Top tier")
    	        );
    	        case "TOP" -> List.of(
    	                new ProviderInfo("OpenAI", true, "High-quality natural voice"),
    	                new ProviderInfo("Google", true, "Cost effective (~$4/1M chars)"),
    	                new ProviderInfo("AWS Polly", true, "Enterprise grade, many voices")
    	        );
    	        default -> List.of();
    	    };
    	
        // In real world → lookup subscription info from DB / License server
        /*return List.of(
                new ProviderInfo("OpenAI", true, "High-quality natural voice"),
                new ProviderInfo("Google", false, "Cost effective (~$4/1M chars)"),
                new ProviderInfo("AWS Polly", false, "Enterprise grade, many voices")
        );*/
    }

}
