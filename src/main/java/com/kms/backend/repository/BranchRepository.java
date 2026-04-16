package com.kms.backend.repository;

import com.kms.backend.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    // Support for /mobile/branches search
    List<Branch> findByPincode(Long pincode);
}
