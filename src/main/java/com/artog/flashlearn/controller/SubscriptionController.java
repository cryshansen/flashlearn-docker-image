package com.artog.flashlearn.controller;

import com.artog.flashlearn.dto.UpgradeRequest;
import com.artog.flashlearn.model.SubscriptionTier;
import com.artog.flashlearn.model.User;
import com.artog.flashlearn.repo.UserRepository;
import com.artog.flashlearn.util.JwtTokenUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;


/**
 * 
 * SubscriptionController = user tiers

* 
 * */

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final UserRepository userRepo;

    public SubscriptionController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/upgrade")
    public ResponseEntity<String> upgradeSubscription(@RequestBody UpgradeRequest request,
                                                      HttpServletRequest httpRequest) {
        try {
            // Extract user email from JWT
            String authHeader = httpRequest.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Missing or invalid token");
            }

            String token = authHeader.substring(7);
            String email = JwtTokenUtil.getEmail(token); // add getEmail() in JwtTokenUtil

            User user = userRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update tier
            user.setSubscriptionTier(SubscriptionTier.valueOf(request.getNewTier().toUpperCase())); //SubscriptionTier.valueOf(subscriptionTier.toUpperCase());
            userRepo.save(user);

            // Issue new JWT with new tier
            String newToken = JwtTokenUtil.generateToken(email, user.getSubscriptionTier().toString());

            return ResponseEntity.ok(newToken);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
