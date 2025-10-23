package com.artog.flashlearn.unit.service;

import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.artog.flashlearn.dto.StudySessionDto;
import com.artog.flashlearn.model.StudySession;
import com.artog.flashlearn.model.User;
import com.artog.flashlearn.repo.StudySessionRepository;
import com.artog.flashlearn.service.StudyService;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class StudySessionServiceTest {

	@Mock
    private StudySessionRepository sessionRepo;

	@InjectMocks
	private StudyService studySessionService;
	
	private StudySessionDto studySessionDto;
	private StudySession studySession;
	private User user;
	
	
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	/*@Test 
	void getSessionByUser_shouldReturnSession() {
		user = new User();
		user.setId(1L);
		when(sessionRepo.findByUserId(1L) .thenReturn(List.of(studySessionDto));
		
	}*/
	
	@Test
	void getSessionById_ShouldReturnSession() {
		studySession = new StudySession();
		studySession.setId(1L);
		
	}
	
}
