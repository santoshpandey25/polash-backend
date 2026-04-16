package com.kms.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "RISK_CATEGORY_MAPPING")
@Data @NoArgsConstructor @AllArgsConstructor
public class RiskCategoryMapping {
    @Id private Long id;
    private Long challenge_count;
}
