package com.kms.backend.service;

import com.kms.backend.model.Branch;
import com.kms.backend.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    public List<Branch> searchBranches(String pincode) {
        // Convert String from JSON to Long for the Database
        return branchRepository.findByPincode(Long.parseLong(pincode));
    }
}
