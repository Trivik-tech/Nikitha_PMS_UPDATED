package com.triviktech.services.auth;

public interface ForgotPasswordService {
    public String genrateOtp(String email);

    public boolean validateotp(String inputotp);

    public boolean resetPassword(String otp, String newPassword);
}
