package com.kms.backend.repository;

import com.kms.backend.model.RiskCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskCategoryRepository extends JpaRepository<RiskCategoryMapping, Long> {}
