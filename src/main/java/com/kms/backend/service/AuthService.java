package com.kms.backend.service;

import com.kms.backend.dto.SignupRequest;
import com.kms.backend.model.Customer;
import com.kms.backend.model.Employees;
import com.kms.backend.repository.CustomerRepository;
import com.kms.backend.repository.EmployeesRepository;
import com.kms.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private EmployeesRepository employeesRepository;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;

    /**
     * 1. SIGNUP: Creates a PENDING user and attempts to send OTP.
     * If email fails due to cloud network restrictions, the user is still saved.
     */
    public void registerUser(SignupRequest request) {
        if (customerRepository.existsById(request.getUserId())) {
            throw new RuntimeException("User ID already exists!");
        }

        Customer customer = new Customer();
        customer.setId(request.getUserId());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        
        // Initial status is PENDING
        customer.setStatus("PENDING_VERIFICATION");

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        customer.setOtp_code(otp);
        customer.setOtp_expiry(LocalDateTime.now().plusMinutes(15));

        // Save to Database first
        customerRepository.save(customer);
        
        // Attempt to send email but don't crash if it fails
        try {
            emailService.sendOtpEmail(customer.getEmail(), otp);
            System.out.println(">>> OTP Email sent successfully to: " + customer.getEmail());
        } catch (Exception e) {
            System.err.println(">>> EMAIL BLOCKER: Railway/Network prevented email delivery.");
            // Print OTP to logs so you can find it in the Railway Dashboard
            System.out.println(">>> [DEBUG LOG] OTP for " + customer.getId() + " is: " + otp);
        }
    }

    /**
     * 2. ACTIVATE: Verifies Signup OTP and enables the user.
     */
    public void activateUser(String userId, String otp) {
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (customer.getOtp_code() != null && 
            customer.getOtp_code().equals(otp) && 
            customer.getOtp_expiry().isAfter(LocalDateTime.now())) {
            
            customer.setStatus("ACTIVE");
            customer.setOtp_code(null); // Clear OTP after success
            customerRepository.save(customer);
        } else {
            throw new RuntimeException("Invalid or Expired OTP");
        }
    }

    /**
     * 3. LOGIN: Checks credentials and ensures account is ACTIVE.
     */
    public String loginMobile(String tempUserId, String rawPassword) {
        Customer customer = customerRepository.findById(tempUserId)
                .orElseThrow(() -> new RuntimeException("Mobile user not found with ID: " + tempUserId));

        if (!"ACTIVE".equals(customer.getStatus())) {
            throw new RuntimeException("Account not activated. Please verify your email first.");
        }

        if (passwordEncoder.matches(rawPassword, customer.getPassword())) {
            return jwtUtil.generateToken(customer.getId());
        } else {
            throw new RuntimeException("Authentication failed: Invalid credentials");
        }
    }

    /**
     * 4. PORTAL LOGIN: Staff authentication.
     */
    public Map<String, Object> loginPortal(String adId, String password) {
        Long solId = Long.parseLong(adId);
        Employees emp = employeesRepository.findById(solId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        if (passwordEncoder.matches(password, emp.getPassword())) {
            String token = jwtUtil.generateToken(adId);
            return Map.of("token", token, "solId", adId);
        }
        throw new RuntimeException("Invalid Credentials");
    }
}
