package com.artog.flashlearn.service;

import com.artog.flashlearn.model.Flashcard;
import com.artog.flashlearn.model.StudySession;
import com.artog.flashlearn.model.User;
import com.artog.flashlearn.repo.FlashcardRepository;
import com.artog.flashlearn.repo.StudySessionRepository;
import com.artog.flashlearn.repo.UserRepository;
import com.artog.flashlearn.dto.FlashcardDto;
import com.artog.flashlearn.dto.StudySessionDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlashcardImportService {

    private final FlashcardRepository flashcardRepository;
    private final StudySessionRepository sessionRepository;
    private final UserRepository userRepo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FlashcardImportService(FlashcardRepository flashcardRepository, StudySessionRepository sessionRepository,UserRepository userRepo) {
        this.flashcardRepository = flashcardRepository;
        this.sessionRepository = sessionRepository;
        this.userRepo = userRepo;
    }

    public void importAllJsonFiles(String folderPath, Long userId) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Folder not found: " + folderPath);
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith("-cards-data.json"));
        if (files == null) return;

        for (File file : files) {
            try {
                // Extract topic from filename
                String filename = file.getName();
                String topic = filename.replace("-cards-data.json", "");

                // Read JSON into FlashcardDto list
                List<FlashcardDto> flashcardDtos = objectMapper.readValue(file, new TypeReference<>() {});
             

                // Build StudySessionDto
                StudySessionDto sessionDto = new StudySessionDto();
                sessionDto.setTopic(topic);
                sessionDto.setUserId(userId);
                //sessionDto.setUsername(username);
                sessionDto.setCreatedAt(LocalDateTime.now());
                sessionDto.setFlashcards(flashcardDtos);
             // Assuming you have a UserRepository
                User user = userRepo.findById(sessionDto.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found with ID " + sessionDto.getUserId()));
                // Map DTO to entity
                StudySession session = new StudySession();
                session.setTopic(sessionDto.getTopic());
                session.setUser(user);
                session.setCreatedAt(sessionDto.getCreatedAt());
                sessionRepository.save(session);

                // Map flashcards
                List<Flashcard> cards = sessionDto.getFlashcards().stream().map(dto -> {
                    Flashcard f = new Flashcard();
                    f.setQuestion(dto.getQuestion());
                    f.setAnswer(dto.getAnswer());
                    f.setColor(dto.getColor());
                    f.setDifficulty(dto.getDifficulty());
                    f.setTopic(dto.getTopic());
                    f.setSession(session);
                    return f;
                }).collect(Collectors.toList());

                flashcardRepository.saveAll(cards);

                System.out.println("✅ Imported session '" + topic + "' with " + cards.size() + " flashcards.");
            } catch (Exception e) {
                System.err.println("❌ Failed to import file " + file.getName() + ": " + e.getMessage());
            }
        }
    }
}
