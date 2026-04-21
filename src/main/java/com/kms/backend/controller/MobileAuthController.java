package com.kms.backend.controller;

import com.kms.backend.dto.LoginRequest;
import com.kms.backend.dto.SignupRequest; // You'll create this DTO
import com.kms.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mobile")
public class MobileAuthController {

    @Autowired
    private AuthService authService;

    // 1. SIGNUP: Collects details and sends OTP
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        try {
            authService.registerUser(request);
            return ResponseEntity.ok(Map.of("message", "Signup successful. Please check your email for OTP."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 2. VERIFY-SIGNUP: Validates OTP and activates account
    @PostMapping("/verify-signup")
    public ResponseEntity<?> verifySignup(@RequestBody Map<String, String> request) {
        try {
            String userId = request.get("userId");
            String otp = request.get("otp");
            authService.activateUser(userId, otp);
            return ResponseEntity.ok(Map.of("message", "Account activated. You can now login."));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    // 3. LOGIN: Now checks if user is ACTIVE
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.loginMobile(request.getTempUserId(), request.getPassword());
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
}
