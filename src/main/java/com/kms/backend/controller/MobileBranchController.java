package com.kms.backend.controller;

import com.kms.backend.model.Branch;
import com.kms.backend.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mobile")
public class MobileBranchController {

    @Autowired
    private BranchService branchService;

    @PostMapping("/branches")
    public ResponseEntity<List<Branch>> getBranches(@RequestBody Map<String, String> request) {
        // Extract pincode from {"pincode": "700001"}
        String pincode = request.get("pincode");
        List<Branch> branches = branchService.searchBranches(pincode);
        return ResponseEntity.ok(branches);
    }
}
