package com.kms.backend.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String userId;
    private String password;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
}
