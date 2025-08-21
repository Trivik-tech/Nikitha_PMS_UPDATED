package com.triviktech.services.auth;

/**
 * Service interface for handling Forgot Password functionality.
 * <p>
 * Provides methods to generate OTP, validate OTP, and reset the password
 * using OTP-based verification. Typically used in authentication and
 * account recovery workflows.
 * </p>
 */
public interface ForgotPasswordService {

    /**
     * Generates a One-Time Password (OTP) for the given email address.
     * <p>
     * This OTP is usually sent to the user's registered email to verify
     * their identity during the password reset process.
     * </p>
     *
     * @param email the email address of the user requesting password reset
     * @return the generated OTP as a {@link String}
     */
    String genrateOtp(String email);

    /**
     * Validates the provided OTP against the system-generated OTP.
     *
     * @param inputotp the OTP entered by the user
     * @return {@code true} if the OTP is valid, {@code false} otherwise
     */
    boolean validateotp(String inputotp);

    /**
     * Resets the user's password after validating the provided OTP.
     *
     * @param otp the OTP provided by the user for verification
     * @param newPassword the new password to be set for the user
     * @return {@code true} if the password was successfully reset,
     *         {@code false} otherwise (e.g., invalid OTP or expired OTP)
     */
    boolean resetPassword(String otp, String newPassword);
}
