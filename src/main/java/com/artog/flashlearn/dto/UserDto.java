package com.artog.flashlearn.dto;

import com.artog.flashlearn.model.SubscriptionTier;
import com.artog.flashlearn.model.User;

public class UserDto {

	
	private Long id;
	private String email ;
	private SubscriptionTier subscriptionTier;
	
	public UserDto(Long id,String email, SubscriptionTier subscriptionTier) {
		this.id = id;
		this.email = email;
		this.subscriptionTier = subscriptionTier;
	}
	
	
	public Long getId() {
		return this.id;
	}
	public String getEmail() {
		
		return this.email;
	}
	public SubscriptionTier getSubscriptionTier() {
		return this.subscriptionTier; 
	}
	
	public static UserDto fromEntity(User user) {
		// TODO Auto-generated method stub
		//return null;
		//if (user == null) return null;
        return new UserDto(
            user.getId(),
            user.getEmail(),
            user.getSubscriptionTier()
        );
	}

}
