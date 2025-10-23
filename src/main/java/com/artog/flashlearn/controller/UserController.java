package com.artog.flashlearn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.artog.flashlearn.dto.LoginRequest;
import com.artog.flashlearn.dto.LoginResponse;
import com.artog.flashlearn.dto.PasswordResetConfirmRequest;
import com.artog.flashlearn.dto.PasswordResetRequest;
import com.artog.flashlearn.dto.SignupRequest;
import com.artog.flashlearn.dto.UpdateUserRequest;
import com.artog.flashlearn.dto.UserDto;
import com.artog.flashlearn.model.SubscriptionTier;
import com.artog.flashlearn.service.UserService;


import java.util.Map;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


import com.artog.flashlearn.model.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;



import com.artog.flashlearn.repo.FlashcardRepository;
import com.artog.flashlearn.repo.StudySessionRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

	    @Autowired
	    private UserService userService;
	    @Autowired
	    private FlashcardRepository flashcardRepository;
	    @Autowired
	    private StudySessionRepository studySessionRepository;

	    @GetMapping("/{id}/subscription")
	    public ResponseEntity<SubscriptionTier> getSubscription(@PathVariable Long id) {
	        return ResponseEntity.ok(userService.getUserSubscription(id));
	    }

	    @PatchMapping("/{id}/subscription")
	    public ResponseEntity<Void> updateSubscription(
	            @PathVariable Long id,
	            @RequestParam SubscriptionTier tier
	    ) {
	        userService.updateUserSubscription(id, tier);
	        return ResponseEntity.ok().build();
	    }
	    
        public UserController(UserService userService, FlashcardRepository flashcardRepository,StudySessionRepository studySessionRepository) {
            this.userService = userService;
            this.flashcardRepository = flashcardRepository;
            this.studySessionRepository = studySessionRepository;
        }

        // 🔹 Signup
        @PostMapping("/signup")
        public ResponseEntity<UserDto> signup(@RequestBody SignupRequest request) {
            return ResponseEntity.ok(userService.signup(request));
        }

        // 🔹 Login
        @PostMapping("/login")
        public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
            return ResponseEntity.ok(userService.login(request));
        }

        // 🔹 Update Profile
        @PutMapping("/{id}")
        public ResponseEntity<UserDto> updateUser(
                @PathVariable Long id,
                @RequestBody UpdateUserRequest request
        ) {
            return ResponseEntity.ok(userService.updateUser(id, request));
        }

        // 🔹 Request Password Reset (sends email with token link)
        @PostMapping("/reset-password/request")
        public ResponseEntity<?> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        	try {
	            userService.requestPasswordReset(request.getEmail());
	            return ResponseEntity.ok("Password reset email sent");
        	}catch(RuntimeException e) {
        		
        		return ResponseEntity.status(HttpStatus.NOT_FOUND)
        				.body("{\"error\":\"" + e.getMessage()+"\"}");
        	}
        }

        // 🔹 Confirm Password Reset (user clicked link from email)
        @PutMapping("/reset-password/confirm")
        public ResponseEntity<?> confirmPasswordReset(@RequestBody PasswordResetConfirmRequest request) {
            userService.confirmPasswordReset(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok("Password reset successful");
        }
        
        @GetMapping("/reset-password/confirm")
        public ResponseEntity<String> confirmToken(@RequestParam String token) {
            boolean valid = userService.isResetTokenValid(token);
            if (valid) {
                return ResponseEntity.ok("Token valid, show reset form");
            } else {
                return ResponseEntity.badRequest().body("Invalid or expired token");
            }
        }


        @GetMapping("/user/{userId}")
        public ResponseEntity<String> exportUserFlashcards(@PathVariable Long userId) throws IOException {
        	List<StudySession> sessions = studySessionRepository. findByUserId(userId);
            
            if (sessions.isEmpty()) {
                return ResponseEntity.ok("⚠️ No study sessions found for user " + userId);
            }
			

            // Prepare ObjectMapper
            ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

            // Create output directory per user
            Path userDir = Paths.get("export/user-" + userId);
            Files.createDirectories(userDir);

            int fileCount = 0;

            for (StudySession session : sessions) {
                List<Flashcard> flashcards = session.getFlashcards();
                if (flashcards == null || flashcards.isEmpty()) continue;

                List<Map<String, Object>> cardsData = new ArrayList<>();

                for (Flashcard f : flashcards) {
                    Map<String, Object> card = new HashMap<>();
                    card.put("topic", f.getTopic());
                    card.put("question", f.getQuestion());
                    card.put("answer", f.getAnswer());
                    card.put("difficulty", f.getDifficulty());
                    card.put("color", f.getColor());
                    card.put("question_audio_path", f.getQuestionAudioPath());
                    card.put("answer_audio_path", f.getAnswerAudioPath());
                    cardsData.add(card);
                }

                Map<String, Object> sessionData = new LinkedHashMap<>();
               // sessionData.put("session", session.getTopic());
                //sessionData.put("user_id", userId);
               // sessionData.put("created_at", session.getCreatedAt().toString());
                sessionData.put("flashcards", cardsData);

                Path out = userDir.resolve(session.getTopic() + "-cards-data.json");
                mapper.writeValue(out.toFile(), sessionData);
                fileCount++;
            }

            return ResponseEntity.ok("✅ Export complete for " + fileCount + " sessions (user " + userId + ")");
        }
            
            
	    
}
