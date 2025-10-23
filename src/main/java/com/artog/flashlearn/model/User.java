package com.artog.flashlearn.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
	
	

	
	
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
   // private String subscriptionTier;
    
    @Enumerated(EnumType.STRING)   // Stores enum as a readable string
    @Column(nullable = false)
    private SubscriptionTier subscriptionTier = SubscriptionTier.FREE;

    public User() {}
	public User(Long id, String email, String password, String subscriptionTier) {

		this.id = id;
		this.email = email;
		this.password = password;
		this.subscriptionTier =  SubscriptionTier.valueOf(subscriptionTier.toUpperCase());

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public SubscriptionTier getSubscriptionTier() {
		return subscriptionTier;
	}

	public void setSubscriptionTier(SubscriptionTier subscriptionTier) {
		this.subscriptionTier = subscriptionTier;
	}


}