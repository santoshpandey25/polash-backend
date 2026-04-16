package com.kms.backend.controller;

import com.kms.backend.model.VerificationReq;
import com.kms.backend.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/mobile")
public class MobileVerificationController {

    @Autowired
    private VerificationService verificationService;

    @PostMapping(value = "/verification", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createVerification(
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("aadhar") String aadhar,
            @RequestParam("consent") boolean consent,
            @RequestParam("solId") String solId,
            @RequestParam("selfiePhoto") MultipartFile selfiePhoto) {
        
        try {
            VerificationReq result = verificationService.initiate(
                email, phone, aadhar, solId, selfiePhoto
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error processing verification: " + e.getMessage());
        }
    }
        // API: /mobile/verification/otp
    // This completes the "Handshake" after the email is sent
    @PostMapping("/verification/otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> body) {
        String requestId = body.get("requestId");
        String otp = body.get("otp");

        // Calls the verifyOtp logic in your VerificationService
        boolean isVerified = verificationService.verifyOtp(requestId, otp);

        if (isVerified) {
            return ResponseEntity.ok(Map.of("verified", true));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(Map.of("verified", false, "error", "Invalid or Expired OTP"));
        }
    }

    @PostMapping(value = "/liveness", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<Map<String, Object>> handleLiveness(
        @RequestParam("requestId") String requestId,
        @RequestParam("startingPic") MultipartFile startingPic,
        @RequestParam("finalPic") MultipartFile finalPic) throws IOException {
    
    // Logic: Save liveness photos and update status
    Map<String, Object> challengeResponse = verificationService.processLiveness(requestId, startingPic, finalPic);
    return ResponseEntity.ok(challengeResponse);
}

@PostMapping("/userdetails")
    public ResponseEntity<?> submitDetails(@RequestBody Map<String, Object> details) {
        verificationService.saveUserDetails((String) details.get("requestId"), details);
        return ResponseEntity.ok(Map.of("accepted", true));
    }
    // Add these to MobileVerificationController.java

// 1. Verify User DID Link
@PostMapping("/verify-did")
public ResponseEntity<?> verifyDid(@RequestBody Map<String, String> body) {
    String requestId = body.get("requestId");
    String didLink = body.get("didLink");
    
    boolean isVerified = verificationService.verifyUserDid(requestId, didLink);
    return ResponseEntity.ok(Map.of("verified", isVerified));
}

// 2. Get User Profile (Secure)
@GetMapping("/profile")
public ResponseEntity<?> getProfile() {
    try {
        // Extract the userId (subject) from the JWT token automatically
        String currentUserId = org.springframework.security.core.context.SecurityContextHolder
                                .getContext().getAuthentication().getName();
                                
        return ResponseEntity.ok(verificationService.getCustomerProfile(currentUserId));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}


}
