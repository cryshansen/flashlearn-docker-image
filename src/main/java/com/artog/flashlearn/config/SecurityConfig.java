package com.artog.flashlearn.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;




import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

        .csrf(csrf -> csrf.disable())          // disable CSRF so POST works in Postman
        .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll()          // allow all requests without auth
        )
        .formLogin(form -> form.disable())     // disable login page
        .httpBasic(basic -> basic.disable());  // disable basic auth
        
        return http.build();
    }
    
    
    @Bean
    public UserDetailsService userDetailsService() {
        // Defines a dummy user to satisfy Spring Boot auto-configuration
        return new InMemoryUserDetailsManager(
                User.withUsername("none")
                    .password("{noop}nopassword")
                    .roles("USER")
                    .build()
        );
    }
    
    
    
 
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(); // strong hashing
        }
    

    
    
}
