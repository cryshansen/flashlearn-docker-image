package com.artog.flashlearn.model;
// com.artog.flashlearn.model

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
public class ResetToken {


	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String token;

	    private LocalDateTime expiryDate;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;

	    public ResetToken() {}

	    public ResetToken(User user, String token, LocalDateTime expiryDate) {
	        this.user = user;
	        this.token = token;
	        this.expiryDate = expiryDate;
	    }

	    public boolean isExpired() {
	        return expiryDate.isBefore(LocalDateTime.now());
	    }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public LocalDateTime getExpiryDate() {
			return expiryDate;
		}

		public void setExpiryDate(LocalDateTime expiryDate) {
			this.expiryDate = expiryDate;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

	}


