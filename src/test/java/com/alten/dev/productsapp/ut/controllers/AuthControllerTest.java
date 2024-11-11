package com.alten.dev.productsapp.ut.controllers;

import com.alten.dev.productsapp.controllers.AuthController;
import com.alten.dev.productsapp.dto.JwtResponse;
import com.alten.dev.productsapp.dto.UserLoginRequest;
import com.alten.dev.productsapp.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    private UserLoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginRequest = new UserLoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");
    }

    @Test
    void createAuthenticationToken_ShouldReturnJwtResponse_WhenCredentialsAreValid() throws Exception {
        // Arrange
        String jwtToken = "fake-jwt-token";
        Mockito.when(authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())))
                .thenReturn(null);  // Mock successful authentication

        Mockito.when(jwtUtil.generateToken(loginRequest.getUsername())).thenReturn(jwtToken);

        // Act
        ResponseEntity<?> response = authController.createAuthenticationToken(loginRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof JwtResponse);
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals(jwtToken, jwtResponse.getToken());
    }

    @Test
    void createAuthenticationToken_ShouldThrowException_WhenInvalidCredentials() throws Exception {
        // Arrange
        Mockito.when(authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())))
                .thenThrow(new BadCredentialsException("Identifiants invalides"));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            authController.createAuthenticationToken(loginRequest);
        });

        assertEquals("Identifiants invalides", exception.getMessage());
    }
}
