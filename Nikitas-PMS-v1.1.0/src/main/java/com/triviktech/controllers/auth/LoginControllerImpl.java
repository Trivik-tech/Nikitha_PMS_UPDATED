package com.triviktech.controllers.auth;

import com.triviktech.payloads.login.Login;
import com.triviktech.services.auth.LoginService;
import com.triviktech.utilities.jwt.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * LoginControllerImpl is a REST controller that implements the LoginController interface.
 *
 * Responsibilities:
 * 1. Handle user login requests by verifying credentials through the LoginService.
 * 2. Generate and return a JWT token upon successful login.
 * 3. Provide an endpoint to retrieve the role of a user based on a provided JWT token.
 *
 * This controller delegates authentication logic to LoginService and token-related
 * operations to JwtService.
 */

@RestController
public class LoginControllerImpl implements LoginController{

    private final LoginService loginService;
    private final JwtService jwtService;

    public LoginControllerImpl(LoginService loginService, JwtService jwtService) {
        this.loginService = loginService;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<Map<String, String>> login(Login login, BindingResult bindingResult) {

        String token = loginService.verifyLogin(login);
        Map<String,String> response=new HashMap<>();
        if(token!=null){
            response.put("token",token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.put("error","Invalid Credentials");

        return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
    }

    @Override
    public ResponseEntity<Map<String, String>> getRole(String token) {
        Map<String,String> response=new HashMap<>();
        response.put("role", jwtService.getRole(token));
        return ResponseEntity.ok(response);
    }
}
