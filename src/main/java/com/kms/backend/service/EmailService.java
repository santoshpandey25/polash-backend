package com.kms.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender; // Note the 'javamail' sub-package
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends a simple text email with the OTP code.
     * @param toEmail The recipient's email address
     * @param otpCode The 6-digit generated OTP
     */
    public void sendOtpEmail(String toEmail, String otpCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            
            // This should match the 'spring.mail.username' in application.properties
            message.setFrom("sanpan2529@gmail.com"); 
            
            message.setTo(toEmail);
            message.setSubject("POLASH Verification Code");
            message.setText("Your verification code is: " + otpCode + 
                            "\n\nThis code is valid for 5 minutes. Please do not share it with anyone.");
            
            mailSender.send(message);
            
        } catch (Exception e) {
            // This will catch errors like "Authentication Failed" if your App Password is wrong
            throw new RuntimeException("Email delivery failed: " + e.getMessage());
        }
    }
}
