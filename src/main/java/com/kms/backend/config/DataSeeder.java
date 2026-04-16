package com.kms.backend.config;

import com.kms.backend.model.*;
import com.kms.backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class DataSeeder implements CommandLineRunner {

    private final BranchRepository branchRepo;
    private final EmployeesRepository employeeRepo;
    private final RiskCategoryRepository riskRepo;
    private final CustomerRepository customerRepo;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(BranchRepository branchRepo, EmployeesRepository employeeRepo, 
                      RiskCategoryRepository riskRepo, CustomerRepository customerRepo, 
                      PasswordEncoder passwordEncoder) {
        this.branchRepo = branchRepo;
        this.employeeRepo = employeeRepo;
        this.riskRepo = riskRepo;
        this.customerRepo = customerRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        
        // 1. Seed RISK_CATEGORY_MAPPING (Parent)
        if (riskRepo.count() == 0) {
            riskRepo.save(new RiskCategoryMapping(1L, 3L)); // Low risk
            riskRepo.save(new RiskCategoryMapping(2L, 5L)); // High risk
            System.out.println("Seeded Risk Categories");
        }

        // 2. Seed BRANCH (Depends on RiskCategory)
        if (branchRepo.count() == 0) {
            RiskCategoryMapping lowRisk = riskRepo.findById(1L).get();
            branchRepo.save(new Branch(101L, 700001L, lowRisk));
            branchRepo.save(new Branch(201L, 110001L, lowRisk));
            System.out.println("Seeded Branches");
        }

        // 3. Seed CUSTOMER (Parent)
        if (customerRepo.count() == 0) {
            Customer c1 = new Customer();
            c1.setId("USER123");
            c1.setEmail("customer@example.com");
            c1.setPhone("9876543210");
            c1.setAadhar("123456789012");
            c1.setPassword(passwordEncoder.encode("customer123"));
            c1.setStatus("ACTIVE");
            c1.setProfileImageLink("/uploads/default.png");
            customerRepo.save(c1);
            System.out.println("Seeded Dummy Customer");
        }

        // 4. Seed EMPLOYEES (Depends on Branch)
        if (employeeRepo.count() == 0) {
            Branch b1 = branchRepo.findById(101L).get();
            Employees staff = new Employees();
            staff.setSol_id(101L); // Using sol_id as adId per your original DDL
            staff.setBranch(b1);
            staff.setName("Bank Staff One");
            staff.setPassword(passwordEncoder.encode("staff123"));
            employeeRepo.save(staff);
            System.out.println("Seeded Portal Staff 101");
        }
    }
}
