package com.kms.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "EMPLOYEES")
@Data @NoArgsConstructor @AllArgsConstructor
public class Employees {
    @Id private Long sol_id;
    
    @ManyToOne @JoinColumn(name = "branch_sol_id", nullable = false)
    private Branch branch;

    @Column(nullable = false) private String name;
    @Column(nullable = false) private String password; // Added for Portal Login
}
