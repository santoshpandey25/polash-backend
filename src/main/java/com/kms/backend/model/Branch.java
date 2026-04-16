package com.kms.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "BRANCH")
@Data @NoArgsConstructor @AllArgsConstructor
public class Branch {
    @Id private Long solId;
    private Long pincode;
    @ManyToOne @JoinColumn(name = "risk_category_id")
    private RiskCategoryMapping riskCategory;
}
