package com.project5.rcrsms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthenticationController {

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/api/login")
    public ResponseEntity<LoginResponse> loginAPI(
            @RequestBody LoginRequest loginRequest) {

        LoginResponse response = new LoginResponse(
                loginRequest.getUsername(),
                encoder.encode(loginRequest.getPassword())
        );

        return ResponseEntity.ok(response);
    }
}
