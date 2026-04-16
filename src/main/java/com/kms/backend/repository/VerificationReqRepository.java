package com.kms.backend.repository;

import com.kms.backend.model.VerificationReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VerificationReqRepository extends JpaRepository<VerificationReq, String> {
    // Used by Portal to fetch requests for a specific branch
    List<VerificationReq> findByBranch_SolId(Long solId);
    
    // Used to find requests by status (e.g., PENDING)
    List<VerificationReq> findByStage(String stage);
}
