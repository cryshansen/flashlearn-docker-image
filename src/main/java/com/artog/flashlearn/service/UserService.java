package com.artog.flashlearn.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.artog.flashlearn.model.User;
import com.artog.flashlearn.dto.LoginRequest;
import com.artog.flashlearn.dto.LoginResponse;
import com.artog.flashlearn.dto.SignupRequest;
import com.artog.flashlearn.dto.UpdateUserRequest;
import com.artog.flashlearn.dto.UserDto;
import com.artog.flashlearn.model.ResetToken;
import com.artog.flashlearn.model.SubscriptionTier;
import com.artog.flashlearn.repo.ResetTokenRepository;
import com.artog.flashlearn.repo.UserRepository;

@Service
public class UserService {
	
	@Autowired
    private UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ResetTokenRepository resetTokenRepository;

    
	public UserService(UserRepository userRepository,
	            PasswordEncoder passwordEncoder,
	            EmailService emailService,
	            ResetTokenRepository resetTokenRepository) {
	    	
				this.userRepository = userRepository;
				this.passwordEncoder = passwordEncoder;
				this.emailService = emailService;
				this.resetTokenRepository = resetTokenRepository;
	}
	
	
	public UserDto updateUser(Long id,UpdateUserRequest request) {
		 // TODO Auto-generated method stub
		return null;
	}
	public UserDto signup(SignupRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
		 throw new RuntimeException("Email already registered");
		}
		
		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setSubscriptionTier(SubscriptionTier.FREE); // default
		userRepository.save(user);
		
		return UserDto.fromEntity(user);
	}
	
	public LoginResponse login(LoginRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
		     .orElseThrow(() -> new RuntimeException("Invalid email or password"));
		
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
		 throw new RuntimeException("Invalid email or password");
		}
		
		// TODO: Generate JWT or session
		return new LoginResponse(user.getId(), user.getEmail(), "dummy-jwt-token");
	}
	
	public void requestPasswordReset(String email) {
		User user = userRepository.findByEmail(email)
		     .orElseThrow(() -> new RuntimeException("Email not found"));
		
		// Generate token
		String token = UUID.randomUUID().toString();
		
		ResetToken resetToken = new ResetToken(user, token, LocalDateTime.now().plusHours(1));
		resetTokenRepository.save(resetToken);
		
		// Send email
		String link = "http://localhost:8080/api/users/reset-password/confirm?token=" + token;
		emailService.send(email, "Password Reset", "Click the link: " + link);
	}
	
	public void confirmPasswordReset(String token, String newPassword) {
		ResetToken resetToken = resetTokenRepository.findByToken(token)
		     .orElseThrow(() -> new RuntimeException("Invalid token"));
		
		if (resetToken.isExpired()) {
			throw new RuntimeException("Token expired");
		}
		
		User user = resetToken.getUser();
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		
		resetTokenRepository.delete(resetToken);
	}
	    
    public SubscriptionTier getUserSubscription(Long userId) {
        return userRepository.findById(userId)
                .map(User::getSubscriptionTier)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void updateUserSubscription(Long userId, SubscriptionTier tier) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setSubscriptionTier(tier);
        userRepository.save(user);
    }


	/*public boolean isResetTokenValid(String token) {
		// TODO Auto-generated method stub
		return false;
	}
*/

	public boolean isResetTokenValid(String token) {
	    Optional<ResetToken> resetOpt = resetTokenRepository.findByToken(token);
	    if (resetOpt.isEmpty()) return false;

	    ResetToken resetToken = resetOpt.get();
	    return resetToken.getExpiryDate().isAfter(LocalDateTime.now());
	}
   
    
    
    
}
