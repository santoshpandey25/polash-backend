package com.kms.backend.service;

import com.kms.backend.model.Customer;
import com.kms.backend.model.Employees;
import com.kms.backend.repository.CustomerRepository;
import com.kms.backend.repository.EmployeesRepository;
import com.kms.backend.security.JwtUtil;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeesRepository EmployeesRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Handles authentication for Mobile Users (Customers)
     * 1. Finds user by tempUserId (id)
     * 2. Matches BCrypt password
     * 3. Returns a JWT token
     */
    public String loginMobile(String tempUserId, String rawPassword) {
        // Find the customer using the ID from your DDL
        Customer customer = customerRepository.findById(tempUserId)
                .orElseThrow(() -> new RuntimeException("Mobile user not found with ID: " + tempUserId));

        // Use passwordEncoder to safely check the password
        if (passwordEncoder.matches(rawPassword, customer.getPassword())) {
            
            // Generate a token specific to this Customer
            return jwtUtil.generateToken(customer.getId());
        } else {
            throw new RuntimeException("Authentication failed: Invalid credentials");
        }
    }

    public Map<String, Object> loginPortal(String adId, String password) {
    Long solId = Long.parseLong(adId);
    Employees emp = EmployeesRepository.findById(solId)
            .orElseThrow(() -> new RuntimeException("Staff not found"));

    if (passwordEncoder.matches(password, emp.getPassword())) {
        String token = jwtUtil.generateToken(adId);
        return Map.of("token", token, "solId", adId);
    }
    throw new RuntimeException("Invalid Credentials");
}

}
