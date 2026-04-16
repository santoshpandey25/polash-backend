package com.kms.backend.repository;

import com.kms.backend.model.DID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DidRepository extends JpaRepository<DID, String> {}
