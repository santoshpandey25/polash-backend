package com.kms.backend.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String tempUserId;
    private String password;
}
