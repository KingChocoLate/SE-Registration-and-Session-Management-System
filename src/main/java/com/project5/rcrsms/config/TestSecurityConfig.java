package com.project5.rcrsms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/register", "/login", "/error").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") 
                .defaultSuccessUrl("/", true) // Redirect to Home after login
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout") // Redirect to Login after logout
                .permitAll()
            );
            
        return http.build();
    }

    // --- THIS IS THE MISSING PIECE ---
    @Bean
    public UserDetailsService userDetailsService() {
        // Create a regular user (Researcher)
        UserDetails user = User.builder()
            .username("user")
            .password("{noop}password") // {noop} tells Spring not to encrypt for testing
            .roles("USER")
            .build();

        // Create an Admin (Conference Chair)
        UserDetails admin = User.builder()
            .username("admin")
            .password("{noop}admin")
            .roles("ADMIN", "CHAIR")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}