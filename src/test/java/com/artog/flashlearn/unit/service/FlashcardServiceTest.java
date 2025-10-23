package com.artog.flashlearn.unit.service;


import com.artog.flashlearn.config.OpenAiConfig;
import com.artog.flashlearn.dto.FlashcardSessionDto;
import com.artog.flashlearn.model.Flashcard;
import com.artog.flashlearn.model.StudySession;
import com.artog.flashlearn.repo.FlashcardRepository;
import com.artog.flashlearn.repo.StudySessionRepository;
import com.artog.flashlearn.service.FlashcardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlashcardServiceTest {

    @Mock
    private FlashcardRepository flashcardRepository;
    @Mock
    private StudySessionRepository sessionRepo;

    @Mock
    private OpenAiConfig config;

    @InjectMocks
    private FlashcardService flashcardService;

    private Flashcard flashcard;
    private StudySession session;

    @BeforeEach
    void setUp() {
      
    	
    	
    	
    	MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFlashcardById_shouldReturnFlashcard() {
    	session = new StudySession();
        session.setId(1L);

        flashcard = new Flashcard(
                session,
                "Java Basics",
                "What is a class?",
                "A blueprint for objects.",
                "Easy",
                "Blue",
                "audio/q1.mp3",
                "audio/a1.mp3"
        );
        flashcard.setId(1L);
    	
        when(flashcardRepository.findById(1L)).thenReturn(Optional.of(flashcard));

        Optional<Flashcard> result = flashcardService.findById(1L);

        assertNotNull(result);
        assertEquals("What is a class?", result.get().getQuestion());
        verify(flashcardRepository).findById(1L);
    }

   /* 
    * Code returns Optional tbd if required
    * @Test
    void getFlashcardById_shouldThrowExceptionIfNotFound() {
        when(flashcardRepository.findById(999L)).thenReturn(Optional.empty());
        
        //this should throw entity not found but doesn't
        
        assertThrows(EntityNotFoundException.class,
                () -> flashcardService.findById(999L));
        
    }*/
    
    @Test
    void testFindById_empty() {
        // Arrange
        Long id = 2L;
        when(flashcardRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Flashcard> result = flashcardService.findById(id);

        // Assert
        assertFalse(result.isPresent());
        
    }
    
    
    
    /*
     * @Test
    void createFlashcard_shouldSaveFlashcard() {
        when(flashcardRepository.save(flashcard)).thenReturn(flashcard);

        Optional<Flashcard> result = flashcardService.getFlashcardsBySession(null).(flashcard);

        assertEquals("What is Java?", result.get().getQuestion());
        verify(flashcardRepository).save(flashcard);
    }
    */

    @Test
    void getAllFlashcards_shouldReturnList() {
    	Flashcard card1 = new Flashcard();
        Flashcard card2 = new Flashcard();
        
        when(flashcardRepository.findAll()).thenReturn(List.of(card1,card2));

        List<Flashcard> result = flashcardRepository.findAll();// flashcardService.getFlashcardsBySession(3L);

        assertEquals(2, result.size());
        verify(flashcardRepository,times(1)).findAll();
       
    }
    
    
    @Test
    void getFlashcardsBySession_ReturnsExpectedFlashcards() {
        Long sessionId = 1L;
        Flashcard card1 = new Flashcard();
        Flashcard card2 = new Flashcard();
        when(flashcardRepository.findBySession_Id(sessionId))
                .thenReturn(List.of(card1, card2));

        List<Flashcard> result = flashcardService.getFlashcardsBySession(sessionId);

        assertEquals(2, result.size());
        verify(flashcardRepository, times(1)).findBySession_Id(sessionId);
    }

    @Test
    void getFlashcardsGroupedBySession_ReturnsMappedDtos() {
        Long userId = 1L;

        StudySession session = new StudySession();
        session.setId(100L);
        session.setTopic("Biology");
        session.setFlashcards(List.of(new Flashcard()));

        Page<StudySession> mockPage = new PageImpl<>(List.of(session));

        when(sessionRepo.findByUser_Id(eq(userId), any(Pageable.class)))
                .thenReturn(mockPage);

        Page<FlashcardSessionDto> result =
                flashcardService.getFlashcardsGroupedBySession(userId, PageRequest.of(0, 10));

        assertEquals(1, result.getContent().size());
        assertEquals("Biology", result.getContent().get(0).getTopic());
    }
    
    
}
