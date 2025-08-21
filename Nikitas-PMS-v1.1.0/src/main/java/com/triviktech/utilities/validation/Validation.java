package com.triviktech.utilities.validation;

/**
 * Utility class for performing common validation operations.
 * Currently includes email validation, but can be extended for other types of validations.
 */
public class Validation {

    /**
     * Validates whether the given string is a properly formatted email address.
     *
     * @param email The email string to validate
     * @return true if the email is non-null, non-empty, and matches the standard email pattern; false otherwise
     */
    public static boolean emailValidation(String email) {
        // Check if email is null or empty
        if (email == null || email.isEmpty()) {
            return false;
        }

        // Regular expression for a valid email format
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

        // Return true if email matches the pattern, false otherwise
        return email.matches(emailRegex);
    }
}
