package com.kms.backend.controller;

import com.kms.backend.model.VerificationReq;
import com.kms.backend.service.AuthService;
import com.kms.backend.repository.VerificationReqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/portal")
@CrossOrigin(origins = "*") // Mandatory for React to work
public class PortalController {

    @Autowired
    private AuthService authService;

    @Autowired
    private VerificationReqRepository requestRepo;

    // --- 1. PORTAL LOGIN API ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            // Uses the loginPortal logic we wrote in AuthService
            Map<String, Object> authResponse = authService.loginPortal(
                body.get("adId"), 
                body.get("password")
            );
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    // --- 2. FETCH ALL REQUESTS API ---
    @PostMapping("/verification-requests")
    public ResponseEntity<List<VerificationReq>> getRequests(@RequestBody Map<String, String> body) {
        Long solId = Long.parseLong(body.get("solId"));
        return ResponseEntity.ok(requestRepo.findByBranch_SolId(solId));
    }

    // --- 3. FETCH CKYC RECORD API ---
    @PostMapping("/ckyc")
    public ResponseEntity<?> getCkyc(@RequestBody Map<String, String> body) {
        String requestId = body.get("requestId");
        VerificationReq req = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        return ResponseEntity.ok(Map.of("recordFound", true, "details", req.getCkyc_record()));
    }

    // --- 4. FACE MATCH RESULTS API ---
    @PostMapping("/face-results")
    public ResponseEntity<?> getFaceResults(@RequestBody Map<String, String> body) {
        String requestId = body.get("requestId");
        VerificationReq req = requestRepo.findById(requestId).orElseThrow();
        return ResponseEntity.ok(req.getFace_matches());
    }

    // --- 5. APPROVE/REJECT DECISION API ---
    @PostMapping("/verification/decision")
    public ResponseEntity<?> recordDecision(@RequestBody Map<String, Object> body) {
        String requestId = (String) body.get("requestId");
        String decision = (String) body.get("decision"); // 'approve' or 'reject'
        String remarks = (String) body.get("remarks");

        VerificationReq req = requestRepo.findById(requestId).orElseThrow();
        req.setStage(decision.equalsIgnoreCase("approve") ? "approved" : "rejected");
        req.setRemarks(remarks);
        req.setDecidedAt(LocalDateTime.now());
        
        requestRepo.save(req);
        return ResponseEntity.ok(Map.of("decision", decision, "status", "success"));
    }
}
