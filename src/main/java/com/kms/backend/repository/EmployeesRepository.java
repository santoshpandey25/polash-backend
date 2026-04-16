package com.kms.backend.repository;

import com.kms.backend.model.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmployeesRepository extends JpaRepository<Employees, Long> {
    // This is used for Portal Login (adId mapping to sol_id)
    Optional<Employees> findById(Long solId);
}
