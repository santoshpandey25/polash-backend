package com.kms.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "DID")
@Data @NoArgsConstructor @AllArgsConstructor
public class DID {
    @Id private String id;

    @OneToOne @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String public_key;

    @Column(columnDefinition = "TEXT")
    private String private_key;

    private String mode; // custodian/non-custodian
}
