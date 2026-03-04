package com.triviktech.controllers.auth;

import com.triviktech.payloads.login.Login;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * LoginController interface defines endpoints for user authentication.
 *
 * Responsibilities:
 * 1. Authenticate a user using credentials (login).
 * 2. Retrieve the role of a user based on the provided JWT token.
 *
 * Base URL: /api/v1/pms/auth
 * Allows cross-origin requests from http://localhost:3000
 */
@RequestMapping("/api/v1/pms/auth")
public interface LoginController {

    /**
     * Authenticates the user with provided login credentials.
     *
     * @param login         Object containing username and password
     * @param bindingResult Captures validation errors if any in the request body
     * @return ResponseEntity containing a map with authentication details (e.g.,
     *         JWT token, username)
     */
    @PostMapping("/login")
    ResponseEntity<Map<String, String>> login(@RequestBody Login login, BindingResult bindingResult);

    /**
     * Retrieves the role of the user associated with the given JWT token.
     *
     * @param token JWT token of the user
     * @return ResponseEntity containing a map with role information (e.g., "role":
     *         "EMPLOYEE")
     */
    @GetMapping("/{token}")
    ResponseEntity<Map<String, String>> getRole(@PathVariable("token") String token);
}
