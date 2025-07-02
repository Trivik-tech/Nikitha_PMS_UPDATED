package com.triviktech.utilities.validation;

public class Validation {


    public static boolean emailValidation(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        // Regular expression for a valid email
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

        return email.matches(emailRegex);
    }


}
