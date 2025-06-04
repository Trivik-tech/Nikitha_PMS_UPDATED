package com.triviktech.controllers.auth;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/v1/pms/auth")
@CrossOrigin(origins = "http://localhost:3000")
public interface ForgotPasswordController {
    // Generate OTP
    @PostMapping("/generate")
    public ResponseEntity<String> generateOtp(@RequestParam String email);

    // Combined: Validate OTP and Reset Password
    @PostMapping("/validate-reset")
    public ResponseEntity<String> validateAndReset(
            @RequestParam String otp,
            @RequestBody(required = false) Map<String, String> newPassword);
}
