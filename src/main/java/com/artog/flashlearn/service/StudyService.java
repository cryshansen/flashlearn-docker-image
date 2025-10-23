package com.artog.flashlearn.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.artog.flashlearn.dto.*;
import com.artog.flashlearn.model.*;
import com.artog.flashlearn.repo.FlashcardRepository;
import com.artog.flashlearn.repo.StudySessionRepository;
import com.artog.flashlearn.repo.UserRepository;



/**
 * 
 * this service is an all encompasing service for the user/ flashcard study sessions  this uses the building block of the flashcardService 
 * and uses sessions for study metrics serving up the session of topics? possible tts implementation and OpenAI service. 
 * it seems should use as a ready repos ans respond but has an Chatbot implementation also
 * */

@Service
public class StudyService {
    
    private final FlashcardRepository flashcardRepo;
    private final StudySessionRepository sessionRepo;
    private final OpenAiService openAiService;
    private final UserRepository userRepo;
    private final AudioService audioService;

    public StudyService(OpenAiService openAiService,
                        StudySessionRepository sessionRepo,
                        FlashcardRepository flashcardRepo,
                        UserRepository userRepo,
                        AudioService audioService) {
        this.openAiService = openAiService;
        this.sessionRepo = sessionRepo;
        this.flashcardRepo = flashcardRepo;
        this.userRepo = userRepo;
        this.audioService = audioService;
        
    }

    public StudySessionDto generateStudyGuide(User user, String topic) {
    	
    	 // 🔧 Hardcode for now since user entity not ready
        String fakeUserEmail = "test@test.com";
     // 🔧 Hardcode for now since user entity not ready
        Long id = 1L;

        String password = "p123";
        //String subscriptionTier = SubscriptionTier.valueOf(SubscriptionTier.FREE.name()); // user.setSubscriptionTier(SubscriptionTier.valueOf(request.getNewTier().toUpperCase()));
        String subscriptionTier = SubscriptionTier.valueOf(SubscriptionTier.FREE.name()).toString();
        //User hc_user = new User(id, fakeUserEmail, password, subscriptionTier);

     // Check if exists or not hard coded if not exist: TODO: need the subscriptionTier for which to use. 
        User hc_user = userRepo.findById(id).orElseGet(() -> {
            User u = new User(null, fakeUserEmail, password, subscriptionTier);
            return userRepo.save(u);
        });

        
        
        // Step 1: Save session
       /* StudySession session = new StudySession(user, topic);
        sessionRepo.save(session);*/
        // Step 1: Save session (bypass user)
        StudySession session = new StudySession();
        session.setTopic(topic);
        session.setUser(hc_user); // if you have a field
        sessionRepo.save(session);

        // Step 2: Get flashcards from OpenAI
        List<FlashcardDto> flashcards = openAiService.generateFlashcards(topic);

        // Step 3: Convert to entities
        List<Flashcard> entities = flashcards.stream()
		                .map(dto -> {
		                    Flashcard card = new Flashcard(session, dto);
		 
		                    try {                       
		                        
		                        // 🔊 Generate Question Audio
		                        Flashcard questionFilePath = audioService.generateQuestionAudioIfMissing(dto.getId());
		                        card.setQuestionAudioPath(questionFilePath.getQuestionAudioPath());
		
		                        // 🔊 Generate Answer Audio
		                        Flashcard answerFilePath = audioService.generateAnswerAudioIfMissing(dto.getId());
		                        card.setAnswerAudioPath(answerFilePath.getAnswerAudioPath());
		                        
		                    } catch (Exception e) {
		                        e.printStackTrace();
		                    }
		
		                    return card;
		                })
		                .toList();
        
        flashcardRepo.saveAll(entities);

        // Step 4: Return DTO
        return new StudySessionDto(
                session.getId(),
                topic,
                hc_user.getEmail(),
                session.getCreatedAt(),
                flashcards
        );
    }
    public List<StudySession> getSessionsByUser(Long userId ) {
        return sessionRepo.findByUserId(userId);
    }

    public List<StudySessionDto> getSessionById(Long id) {
        return sessionRepo.findById(id)
        		  .stream()
                  .map(this::toDto)
                  .toList();
        
        
    }
    private StudySessionDto toDto(StudySession session) {
    	//Long sessionId, String topic, String username, LocalDateTime createdAt, List<FlashcardDto> flashcards) {
        return new StudySessionDto(
                session.getId(),
                session.getTopic(),
                session.getUser().getId(),
                session.getCreatedAt()
        );
    }
    /**
     * 🔹 Launch async audio generation for all flashcards missing audio.
     * This runs in a background thread.
     */
    @Async
    @Transactional
    public void backfillMissingAudio() {
        List<Flashcard> flashcards = flashcardRepo.findAll();

        System.out.println("🔊 Starting audio backfill for " + flashcards.size() + " flashcards...");

        for (Flashcard card : flashcards) {
            boolean updated = false;
            try {
                // 🔹 Question audio
                if (card.getQuestionAudioPath() == null || card.getQuestionAudioPath().isEmpty()) {
                    Flashcard updatedQuestion = audioService.generateQuestionAudioIfMissing(card.getId());
                    card.setQuestionAudioPath(updatedQuestion.getQuestionAudioPath());
                    updated = true;
                }

                // 🔹 Answer audio
                if (card.getAnswerAudioPath() == null || card.getAnswerAudioPath().isEmpty()) {
                    Flashcard updatedAnswer = audioService.generateAnswerAudioIfMissing(card.getId());
                    card.setAnswerAudioPath(updatedAnswer.getAnswerAudioPath());
                    updated = true;
                }

                if (updated) {
                	flashcardRepo.save(card);
                    System.out.println("✅ Audio generated for Flashcard ID " + card.getId());
                }

            } catch (Exception e) {
                System.err.println("⚠️ Failed audio for card " + card.getId() + ": " + e.getMessage());
            }
        }

        System.out.println("🎉 Audio backfill complete!");
    }
    
    @Async
    @Transactional
    public void backfillMissingAudioBySession(Long sessionId) {
        List<Flashcard> flashcards = flashcardRepo.findBySession_Id(sessionId);
        System.out.println("🔊 Starting backfill for session " + sessionId + " (" + flashcards.size() + " flashcards)");
        //TODO: turn into method for both session and flashcard audio backfill. 
        for (Flashcard card : flashcards) {
            boolean updated = false;
            try {
                // 🔹 Question audio
                if (card.getQuestionAudioPath() == null || card.getQuestionAudioPath().isEmpty()) {
                    Flashcard updatedQuestion = audioService.generateQuestionAudioIfMissing(card.getId());
                    card.setQuestionAudioPath(updatedQuestion.getQuestionAudioPath());
                    updated = true;
                }

                // 🔹 Answer audio
                if (card.getAnswerAudioPath() == null || card.getAnswerAudioPath().isEmpty()) {
                    Flashcard updatedAnswer = audioService.generateAnswerAudioIfMissing(card.getId());
                    card.setAnswerAudioPath(updatedAnswer.getAnswerAudioPath());
                    updated = true;
                }

                if (updated) {
                	flashcardRepo.save(card);
                    System.out.println("✅ Audio generated for Flashcard ID " + card.getId());
                }

            } catch (Exception e) {
                System.err.println("⚠️ Failed audio for card " + card.getId() + ": " + e.getMessage());
            }
        }
    }

}
