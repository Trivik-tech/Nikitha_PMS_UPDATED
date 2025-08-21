package com.triviktech.controllers.auth;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ForgotPasswordController interface defines endpoints for handling
 * password recovery processes such as generating OTP, validating OTP,
 * and resetting passwords.
 *
 * Base URL: /api/v1/pms/auth
 * Allows cross-origin requests from http://localhost:3000
 */
@RequestMapping("/api/v1/pms/auth")
@CrossOrigin(origins = "http://localhost:3000")
public interface ForgotPasswordController {

    /**
     * Generates a One-Time Password (OTP) for the given email.
     *
     * @param email The email address of the user requesting password reset
     * @return ResponseEntity containing a message about OTP status
     */
    @PostMapping("/generate")
    public ResponseEntity<String> generateOtp(@RequestParam String email);

    /**
     * Validates the OTP provided by the user and optionally resets the password.
     *
     * Combined endpoint for:
     * 1. OTP validation
     * 2. Password reset if a new password is provided
     *
     * @param otp The OTP sent to the user's email
     * @param newPassword Optional map containing the new password, e.g., {"password": "newPass123"}
     * @return ResponseEntity containing a message about the success or failure of OTP validation
     *         and/or password reset
     */
    @PostMapping("/validate-reset")
    public ResponseEntity<String> validateAndReset(
            @RequestParam String otp,
            @RequestBody(required = false) Map<String, String> newPassword);
}
