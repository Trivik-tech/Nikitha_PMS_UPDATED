package com.triviktech.controllers.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.triviktech.services.auth.ForgotPasswordService;

/**
 * ForgotPasswordControllerImpl is a REST controller implementation of the
 * ForgotPasswordController interface.
 *
 * Responsibilities:
 * 1. Handle requests to generate a One-Time Password (OTP) for users who
 *    have forgotten their password.
 * 2. Validate the provided OTP and optionally reset the user's password
 *    if a new password is supplied.
 *
 * This controller delegates the actual business logic to the ForgotPasswordService.
 *
 * Base URL mappings are inherited from the ForgotPasswordController interface.
 */

@RestController
public class ForgotPasswordControllerImpl implements ForgotPasswordController {
    @Autowired
    private ForgotPasswordService forgotPasswordService;

    public ResponseEntity<String> generateOtp(@RequestParam String email) {
        String otp = forgotPasswordService.genrateOtp(email);
        return ResponseEntity.ok("OTP sent successfully: " + otp);
    }

    @Override
    public ResponseEntity<String> validateAndReset(
            @RequestParam String otp,
            @RequestBody(required = false) Map<String, String> newPassword) {

        boolean isValid = forgotPasswordService.validateotp(otp.trim());
        if (!isValid) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP.");
        }

        if (newPassword != null && !newPassword.get("password").isEmpty()) {
            boolean isReset = forgotPasswordService.resetPassword(otp, newPassword.get("password"));
            if (isReset) {
                return ResponseEntity.ok("Password reset successful.");
            } else {
                return ResponseEntity.badRequest().body("OTP valid, but password reset failed.");
            }
        }

        return ResponseEntity.ok("OTP validation successful.");
    }

}
