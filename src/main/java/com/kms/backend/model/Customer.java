package com.kms.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "CUSTOMER")
@Data @NoArgsConstructor @AllArgsConstructor
public class Customer {
    @Id private String id; // tempUserId
    @Column(nullable = false) private String password;
    @Column(nullable = false) private String email;
    @Column(nullable = false) private String phone;
    private String address;
    private String aadhar;
    private String pan;
    private String firstName;
    private String lastName;
    private Boolean fatca;
    private String profileImageLink;
    private String status;

    // Recommendation: Integrated Nominee fields
    private String nomineeName;
    private String nomineeEmail;
    private String nomineePhone;
    private String nomineeRelation;
}
