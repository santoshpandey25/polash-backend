package com.kms.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Table(name = "VERIFICATION_REQ")
@Data @NoArgsConstructor @AllArgsConstructor
public class VerificationReq {
    @Id 
    private String id;
    
    private String stage; // Enum handled as String

    @ManyToOne @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne @JoinColumn(name = "approving_employee_id")
    private Employees approvingEmployee;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> ckyc_record;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Map<String, String>> face_matches;

    // --- ADD THESE THREE FIELDS FOR OTP ---
    private String otpCode;          // To store the 6-digit code
    private LocalDateTime otpExpiry; // To handle the 5-minute timeout
    private Integer otpAttempts;     // Optional: To prevent brute force (e.g., max 3 tries)
    // --------------------------------------

    private String remarks;
    private String startingPicUrl;
    private String finalPicUrl;
    private LocalDateTime decidedAt;
}
