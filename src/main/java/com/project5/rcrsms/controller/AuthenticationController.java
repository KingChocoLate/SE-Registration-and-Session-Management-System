package com.project5.rcrsms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project5.rcrsms.dto.LoginRequest;
import com.project5.rcrsms.dto.LoginResponse;

@Controller
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/api/login")
    public ResponseEntity<LoginResponse> loginAPI(
            @RequestBody LoginRequest loginRequest) {
        
        try {
            // Authenticate against the real database
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );
            
            // Authentication successful
            LoginResponse response = new LoginResponse(
                    loginRequest.getUsername(),
                    "Authentication successful"
            );
            return ResponseEntity.ok(response);
            
        } catch (AuthenticationException e) {
            // Authentication failed
            return ResponseEntity.status(401).body(
                new LoginResponse(null, "Invalid credentials")
            );
        }
    }
}
