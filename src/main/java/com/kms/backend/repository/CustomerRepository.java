package com.kms.backend.repository;

import com.kms.backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    // This is used for Mobile Login
    Optional<Customer> findById(String id);
}
