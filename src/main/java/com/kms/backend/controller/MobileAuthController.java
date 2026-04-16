package com.kms.backend.controller;

import com.kms.backend.dto.LoginRequest;
import com.kms.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mobile") // Matches your OpenAPI spec
public class MobileAuthController {

    @Autowired
    private AuthService authService;

    /**
     * Endpoint for Android App to Login.
     * It receives tempUserId and password.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Calls the Service logic we wrote earlier
            String token = authService.loginMobile(request.getTempUserId(), request.getPassword());
            
            // Returns the JWT token as a JSON object: {"token": "eyJ..."}
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            // Returns 401 Unauthorized if login fails (wrong user or password)
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
}
