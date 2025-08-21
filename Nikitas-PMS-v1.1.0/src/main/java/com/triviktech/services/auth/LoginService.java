package com.triviktech.services.auth;

import com.triviktech.payloads.login.Login;

/**
 * Service interface for handling user login operations.
 * <p>
 * This interface defines the contract for verifying login credentials
 * and returning the login result (e.g., success, failure, or token).
 * </p>
 */
public interface LoginService {

    /**
     * Verifies the provided user login credentials.
     * <p>
     * This method checks whether the credentials provided in the
     * {@link Login} request are valid. If successful, it returns
     * a status message or an authentication token depending on the
     * implementation.
     * </p>
     *
     * @param login the {@link Login} object containing user credentials
     *              (e.g., username/email and password).
     * @return a {@link String} representing the login result (e.g., success message,
     *         failure reason, or authentication token).
     */
    String verifyLogin(Login login);

}
