package com.kms.backend.service;

import com.kms.backend.model.Customer;
import com.kms.backend.model.VerificationReq;
import com.kms.backend.repository.VerificationReqRepository;
import com.kms.backend.repository.BranchRepository;
import com.kms.backend.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Map;
import java.util.Random;

@Service
public class VerificationService {

    @Autowired private VerificationReqRepository requestRepo;
    @Autowired private BranchRepository branchRepo;
    @Autowired private EncryptionService encryptionService;
    @Autowired private EmailService emailService;
    @Autowired private FileStorageService fileStorageService; // Added this dependency
     @Autowired private CustomerRepository customerRepo;
    /**
     * Step 1: Initial submission from Mobile App
     */
    public VerificationReq initiate(String email, String phone, String aadhar, 
                                   String solId, MultipartFile selfie) throws IOException {
        
        // 1. Save Selfie Photo using the dedicated service
        String selfiePath = fileStorageService.save(selfie);

        // 2. Encrypt sensitive details (Aadhar/Phone)
        String encryptedAadhar = encryptionService.encrypt(aadhar);
        String encryptedPhone = encryptionService.encrypt(phone);

        // 3. Generate 6-digit OTP and set 5-minute expiry
        String generatedOtp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        // 4. Prepare the Verification Request object
        VerificationReq req = new VerificationReq();
        req.setId(UUID.randomUUID().toString());
        req.setStage("pending_otp");
        req.setStartingPicUrl(selfiePath); // Store the path returned by service
        
        req.setOtpCode(generatedOtp);
        req.setOtpExpiry(expiry);
        
        branchRepo.findById(Long.parseLong(solId)).ifPresent(req::setBranch);

        // 5. Save to Database
        VerificationReq savedReq = requestRepo.save(req);

        // 6. Send the OTP to User's Email
        try {
            emailService.sendOtpEmail(email, generatedOtp);
            System.out.println(">>> OTP sent to email: " + email);
           // System.out.println(">>> OTP is: " + generatedOtp);
        } catch (Exception e) {
            System.err.println(">>> Email delivery failed: " + e.getMessage());
        }

        return savedReq;
    }

    /**
     * Step 2: Verify the OTP sent to user
     */
    public boolean verifyOtp(String requestId, String userOtp) {
        return requestRepo.findById(requestId)
            .map(req -> {
                if (req.getOtpCode().equals(userOtp) && 
                    req.getOtpExpiry().isAfter(LocalDateTime.now())) {
                    
                    req.setStage("otp_verified");
                    requestRepo.save(req);
                    return true;
                }
                return false;
            }).orElse(false);
    }

    /**
     * Step 3: Handle Liveness Detection Photos
     */
    public Map<String, Object> processLiveness(String requestId, MultipartFile start, MultipartFile end) throws IOException {
        VerificationReq req = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        // Save photos using fileStorageService
        String startPath = fileStorageService.save(start);
        String endPath = fileStorageService.save(end);

        req.setStartingPicUrl(startPath);
        req.setFinalPicUrl(endPath);
        req.setStage("liveness_complete");
        requestRepo.save(req);

        return Map.of(
            "totalChallenges", 1,
            "currentChallenge", "SMILE",
            "detectionPhaseComplete", true
        );
    }
    public void saveUserDetails(String requestId, Map<String, Object> details) {
        VerificationReq req = requestRepo.findById(requestId).orElseThrow();
        req.setCkyc_record(details);
        req.setStage("completed");
        requestRepo.save(req);
    }
    // Add to VerificationService.java

public boolean verifyUserDid(String requestId, String didLink) {
    return requestRepo.findById(requestId).map(req -> {
        if (didLink != null && !didLink.isEmpty()) {
            req.setStage("did_verified");
            requestRepo.save(req);
            return true;
        }
        return false;
    }).orElse(false);
}

public Customer getCustomerProfile(String userId) {
    // Fetches the full profile based on the ID extracted from JWT
    return customerRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
}

}
