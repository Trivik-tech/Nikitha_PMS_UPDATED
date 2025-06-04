package com.triviktech.controllers.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.triviktech.services.auth.ForgotPasswordService;

@RestController
public class ForgotPasswordControllerImpl implements ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    public ForgotPasswordControllerImpl(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    @Override
    public ResponseEntity<String> generateOtp(@RequestParam String email) {
        String otp = forgotPasswordService.genrateOtp(email);
        return ResponseEntity.ok("OTP sent successfully: " + otp);
    }

    @Override
    public ResponseEntity<String> validateAndReset(
            @RequestParam String otp,
            @RequestBody(required = false) Map<String, String> newPassword) {

        boolean isValid = forgotPasswordService.validateotp(otp);
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
