package com.alten.dev.productsapp.controllers;

import com.alten.dev.productsapp.dto.JwtResponse;
import com.alten.dev.productsapp.dto.UserLoginRequest;
import com.alten.dev.productsapp.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil; // Utilitaire pour créer le token JWT
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserLoginRequest loginRequest) throws Exception {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

        final String jwt = jwtUtil.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
