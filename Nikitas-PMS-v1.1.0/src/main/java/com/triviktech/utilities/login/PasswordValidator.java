package com.triviktech.utilities.login;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for validating password strength.
 *
 * This class checks if a given password meets the defined security criteria:
 * - Minimum 8 characters
 * - At least one letter (uppercase or lowercase)
 * - At least one digit
 * - At least one special character from the allowed set (!@#$%^&*()_+[]{}|;:'",.<>?/)
 */
@Component
public class PasswordValidator {

    // Regular expression pattern to validate password strength
    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\[\\]{}|;:'\",.<>?/]).{8,}$";

    /**
     * Validates whether the provided password meets the required criteria.
     *
     * @param password the password string to validate
     * @return true if the password is valid according to the pattern, false otherwise
     */
    public boolean validatePassword(String password){
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
