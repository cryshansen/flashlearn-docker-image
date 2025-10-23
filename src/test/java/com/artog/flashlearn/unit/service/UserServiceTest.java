package com.artog.flashlearn.unit.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
//import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;



import org.mockito.*;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

import com.artog.flashlearn.dto.LoginRequest;
import com.artog.flashlearn.dto.LoginResponse;
import com.artog.flashlearn.dto.SignupRequest;
import com.artog.flashlearn.dto.UserDto;
import com.artog.flashlearn.model.ResetToken;
import com.artog.flashlearn.model.User;
import com.artog.flashlearn.repo.UserRepository;
import com.artog.flashlearn.repo.ResetTokenRepository;
import com.artog.flashlearn.service.EmailService;
import com.artog.flashlearn.service.UserService;

public class UserServiceTest {

	@Mock
	private UserRepository userRepo;
	
	@Mock
	private ResetTokenRepository resetRepo;
	
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private EmailService emailService;
    
	@InjectMocks
	private UserService userService;
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	//------SIGNUP ---------
	@Test
	void signup_shouldCreateNewUser() {
		
		SignupRequest  request = new SignupRequest();
		request.setEmail("test@example.com");
		request.setPassword("secret");
		
		when(userRepo.existsByEmail(request.getEmail())).thenReturn(false);
		when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedSecret");
		
		UserDto result = userService.signup(request);
		assertEquals("test@example.com", result.getEmail());
		verify(userRepo).save(any(User.class));
		
		
		
		
	}
	@Test 
	void signup_shouldThrowErrorWhenEmailAlreadyExists(){
		
		SignupRequest signupReq = new SignupRequest();
		signupReq.setEmail("example@example.com");
		when(userRepo.existsByEmail(signupReq.getEmail())).thenReturn(true);
		
		assertThrows(RuntimeException.class, () -> userService.signup(signupReq));
		
	}
	
	
	// -----LOGIN ------------------
	
	@Test
	void login_shouldReturnResponseWhenCredentialsValid() {
		
		LoginRequest request = new LoginRequest();
		request.setEmail("test@example.com");
		request.setPassword("secret");
		
		User user = new User();
		user.setEmail("test@example.com");
		user.setPassword("encodedSecret");
		user.setId(1L);
		
		when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));
		when(passwordEncoder.matches("secret", "encodedSecret")).thenReturn(true);
		
		LoginResponse response = userService.login(request);
		
		assertEquals("test@example.com", response.getEmail());
		assertEquals("dummy-jwt-token" , response.getToken());
		
	}
	
	@Test
	void login_shouldThrowWhenEmailNotFound() {
		
		LoginRequest request = new LoginRequest();
		request.setEmail("missing@example.com");
		request.setPassword("secret");
		
		when(userRepo.findByEmail("missing@example.com")).thenReturn(Optional.empty());
		
		assertThrows(RuntimeException.class, () -> userService.login(request));
		
		
	}
	
	@Test
	void login_shouldThrowWhenPasswordInvalid() {
		LoginRequest request = new LoginRequest();
		request.setEmail("test@example.com");
		request.setPassword("wrong");
		
		User user = new User();
		user.setEmail("test@example.com");
		user.setPassword("encodedSecret");
		
		when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));
		
		assertThrows(RuntimeException.class, () -> userService.login(request));
		
		
	}
	
	// ------------------ PASSWORD REST REQUEST ------------------
	@Test
	void  requestPasswordReset_shouldGenerateTokenAndSendEmail() {
		
			User user = new User();
			
			user.setEmail("test@example.com");
			when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));
			when(resetRepo.save(ResetToken.class)).thenAnswer(invocation -> invocation.getArgument(0));
			//when(resetRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
			
			userService.requestPasswordReset("test@example.com");
			verify(resetRepo).save(any(ResetToken.class));
			verify(emailService).send(eq("test@example.com"), anyString(), contains("Click the link:"));
			
	}
	@Test
	void requestPasswordReset_shouldThrowWhenEmailNotFound() {
		when(userRepo.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
		
		assertThrows(RuntimeException.class, () -> userService.requestPasswordReset("notfound@example.com"));
		
	}
	
	// -------------- CONFIRM PASSWORD -----------------
	
	@Test
	void confirmPasswordReset_shouldUpdatePasswordAndDeleteToken() {
		User user = new User();
		user.setEmail("test@example.com");
		ResetToken token = new ResetToken( user, "token123", LocalDateTime.now().plusHours(1));
		
		when(resetRepo.findByToken("token123")).thenReturn(Optional.of(token));
		when(passwordEncoder.encode("newPass")).thenReturn("encodedNew");
		
		userService.confirmPasswordReset("token123","newPass");
		
		verify(userRepo).save(user);
		verify(resetRepo).delete(token);
		assertEquals("encodedNew", user.getPassword());
	}
	
	@Test
	void confirmPasswordReset_shouldThrowWhenTokenInvalid() {
		
		when(resetRepo.findByToken("badToken")).thenReturn(Optional.empty());
		
		assertThrows(RuntimeException.class, () -> userService.confirmPasswordReset("badToken", "newPass"));
	}
	
	
	@Test
	void confirmPasswordReset_shouldThrowWhenTokenExpired() {
		User user = new User();
		ResetToken token = new ResetToken(user,"token123", LocalDateTime.now().minusMinutes(5));
		when(resetRepo.findByToken("token123")).thenReturn(Optional.of(token));
		
		assertThrows(RuntimeException.class, () -> userService.confirmPasswordReset("token123", "newPass"));
		
	}
	
	
	//-------- VALIDATE RESET TOKEN -----
	
	@Test
	void isResetTokenValid_shouldReturnTrueForActiveToken() {
		
		ResetToken token = new ResetToken(null, "token123", LocalDateTime.now().plusMinutes(10));
		
		when(resetRepo.findByToken("token123")).thenReturn(Optional.of(token));
		
		assertTrue(userService.isResetTokenValid("token123"));
		
		
	}
	
	@Test
	void isResetTokenValid_shouldReturnFalseForExpiredToken() {
		ResetToken token = new ResetToken(null, "token123", LocalDateTime.now().minusMinutes(10));
		
		when(resetRepo.findByToken("token123")).thenReturn(Optional.of(token));
		
		assertFalse(userService.isResetTokenValid("token123"));
		
	}
	
	@Test
	void isResetTokenValid_shouldReturnFalseWhenTokenNotFound() {
		when(resetRepo.findByToken("unknown")).thenReturn(Optional.empty());
		assertFalse(userService.isResetTokenValid("unknown"));
	}
	
	
	
	
}
