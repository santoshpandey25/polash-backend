package com.kms.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "CHALLENGES")
@Data @NoArgsConstructor @AllArgsConstructor
public class Challenges {
    @Id private Long id;
    private String tag;
}
