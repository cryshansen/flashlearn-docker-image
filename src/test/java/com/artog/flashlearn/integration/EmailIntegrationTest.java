package com.artog.flashlearn.integration;

import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.Matchers.containsString;

import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//🔄 Updated import for new Spring Boot 3.5.5 doesnt contain testmockbean yet. but is deprecated since 3.4 continue to use mockbean for now
//import org.springframework.boot.test.mock.mockito.TestMockBean;



import java.util.Optional;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.artog.flashlearn.model.ResetToken;
import com.artog.flashlearn.model.User;
import com.artog.flashlearn.repo.ResetTokenRepository;
import com.artog.flashlearn.repo.UserRepository;
import com.artog.flashlearn.service.EmailService;



@SuppressWarnings("unused")
@SpringBootTest
@AutoConfigureMockMvc
public class EmailIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	

    @MockBean
    private UserRepository userRepo;  // this replaces the real bean in context
    
    @MockBean
    private ResetTokenRepository resetTokenRepository;


    @MockBean
    private EmailService emailService;
 // ✅ Using @MockBean until @TestMockBean is actually available in Spring Boot 3.6+
 // 🧩 Using @EnabledIfEnvironmentVariable so the test skips gracefully on CI
 // ⚡ Updated .save(any(User.class)) to fix type mismatch
 // 💡 Keep MailHog integration in separate test class
 // 🧹 Consider replacing CoreMatchers with Matchers for modern Hamcrest import

    @BeforeEach
    void checkMailhogEnabled() {
        assumeTrue(
            "true".equalsIgnoreCase(System.getenv("MAILHOG_ENABLED")),
            "Skipping test because MAILHOG_ENABLED is not true"
        );
    }


    // ✅ OPTIONAL: Skip the test gracefully in CI if no local MailHog or SMTP available
    // This uses a GitHub Action env var check (e.g. set CI=true automatically)
    @Test
   // @EnabledIfEnvironmentVariable(named = "CI", matches = "false") //works locally
    // disabled due to mismatch in github -- ignore @EnabledIfEnvironmentVariable(named = "MAILHOG_ENABLED", matches = "true") //for github actions

	void shouldSendEmail_WhenUserExists() throws Exception {
	    User user = new User();
	    user.setEmail("test@example.com");
	    when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));
	
	    mockMvc.perform(post("/api/users/reset-password/request")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("{\"email\":\"test@example.com\"}"))
	            .andExpect(status().isOk());
	
	    // ✅ Verify that the email service was called correctly
	    verify(emailService).send(eq("test@example.com"), eq("Password Reset"), contains("Click the link:"));
	}
	
	@Test
	void shouldNotSendEmail_WhenUserDoesNotExist() throws Exception {
	    // 🧩 Arrange: mock repository to return empty for the given email  stub is same as whenEmailNotFound
	    when(userRepo.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

	    // 🚀 Act: call the password reset endpoint
	    mockMvc.perform(post("/api/users/reset-password/request")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("{\"email\":\"nonexistent@example.com\"}"))
	            .andExpect(status().isNotFound()) // or .isBadRequest() depending on your controller
	            .andExpect(content().string(containsString("Email not found")));

	    // 🧠 Assert: verify that NO email was sent
	    verify(emailService, never()).send(anyString(), anyString(), anyString());
	}
	
	@Test
	void shouldResetPassword_WhenTokenValid() throws Exception{
		User user = new User();
		user.setId(1L);
		user.setEmail("user@example.com");
		user.setPassword("oldPassword");
		
		ResetToken token = new ResetToken(user,"valid-token", LocalDateTime.now().plusHours(1));
		
		when(resetTokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));
		
		
		 // 🚀 Act: call the password reset confirm endpoint
	    mockMvc.perform(put("/api/users/reset-password/confirm")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content("{\"token\":\"valid-token\",\"newPassword\":\"newPass123\"}"))
	            .andExpect(status().isOk()) // or .isBadRequest() depending on your controller
	            .andExpect(content().string(containsString("Password reset successful")));
				
				
		verify(userRepo).save(any(User.class));		
		/*verify(userRepo).save(argThat(user ->
			    user.getPassword().equals("newPass123")
			));*/

		
	}
	@Test
	void shouldFailToResetPassword_WhenTokenExpired() throws Exception{
		User user = new User();
		ResetToken expired = new ResetToken(user, "expired-token", LocalDateTime.now().minusMinutes(5));
		when(resetTokenRepository.findByToken("expired-token")).thenReturn(Optional.of(expired));
		
		
		mockMvc.perform(get("/api/users/reset-password/confirm")
				.param("token","expired-token"))
				//.contentType(MediaType.APPLICATION_JSON)
				//.content("{\"token\":\"expired-token\",\"newPassword\":\"abc123\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Invalid or expired token")));
		
		verify(userRepo,never()).save(any(User.class));
		
	}


}
