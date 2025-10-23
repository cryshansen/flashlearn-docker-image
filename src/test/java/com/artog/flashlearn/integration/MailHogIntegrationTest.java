package com.artog.flashlearn.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import com.artog.flashlearn.model.SubscriptionTier;
import com.artog.flashlearn.model.User;
import com.artog.flashlearn.repo.ResetTokenRepository;
import com.artog.flashlearn.repo.StudySessionRepository;
import com.artog.flashlearn.repo.UserRepository;
import com.artog.flashlearn.service.UserService;

@SpringBootTest
@ActiveProfiles("test-mailhog") //separate test profile
@Transactional
@Tag("mailhog")
public class MailHogIntegrationTest {
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ResetTokenRepository resetTokenRepo;
	@Autowired
	private StudySessionRepository  studySessionRepo;
	
	 @Value("${mailhog.api.url}")
	 private String mailHogApiUrl;

    @Value("${mailhog.api.clear-url}")
    private String mailHogClearUrl;
	
	//private final String MAILHOG_API = "http://localhost:8025/api/v2/messages";
	
	@BeforeEach
	void setup() {
		resetTokenRepo.deleteAll();
	    // delete sessions first, then users
	    studySessionRepo.deleteAll(); 
		userRepo.deleteAll();
		new RestTemplate().delete(mailHogClearUrl);
	}
	
	@Test
	void shouldSendEmailAndBeReceivedByMailHog() throws Exception {
		
		User user = new User();
		user.setEmail("test@example.com");
		user.setPassword("pass123");
		user.setSubscriptionTier(SubscriptionTier.FREE);
		userRepo.save(user);
		
		userService.requestPasswordReset("test@example.com");
		
	
		// Wait a bit to let email deliver
		
		Thread.sleep(1000);
		
		//Call Mailhog API 
		
		RestTemplate restTemplate = new RestTemplate();
		
		/*
		 * 
		ResponseEntity<String> response = restTemplate.getForEntity(mailHogApiUrl, String.class);
		 
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("test@example.com");
		assertThat(response.getBody()).contains("Click the link:");
		
		*/
		
		Awaitility.await().atMost(5, TimeUnit.SECONDS)
        .untilAsserted(() -> {
            ResponseEntity<String> r = restTemplate.getForEntity(mailHogApiUrl, String.class);
            assertThat(r.getBody()).contains("test@example.com");
    		assertThat(r.getBody()).contains("Click the link:");
        });
		
		
		
	}
	

}
