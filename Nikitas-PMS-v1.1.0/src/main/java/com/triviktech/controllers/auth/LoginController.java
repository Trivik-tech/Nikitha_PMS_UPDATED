package com.triviktech.controllers.auth;

import com.triviktech.payloads.login.Login;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/v1/pms/auth")
@CrossOrigin(origins = "http://localhost:3000")
public interface LoginController {

    @PostMapping("/login")
    ResponseEntity<Map<String, String>> login( @RequestBody Login login, BindingResult bindingResult);

    @GetMapping("/{token}")
    ResponseEntity<Map<String, String>> getRole(@PathVariable String token);
}
