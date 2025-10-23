package com.artog.flashlearn.dto;

public class UpdateUserRequest {
	private String email;
	private String password; // optional update
	private String subscriptionTier; // optional update
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSubscriptionTier() {
		return subscriptionTier;
	}
	public void setSubscriptionTier(String subscriptionTier) {
		this.subscriptionTier = subscriptionTier;
	}
	
	
}

