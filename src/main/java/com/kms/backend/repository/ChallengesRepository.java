package com.kms.backend.repository;

import com.kms.backend.model.Challenges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengesRepository extends JpaRepository<Challenges, Long> {}
