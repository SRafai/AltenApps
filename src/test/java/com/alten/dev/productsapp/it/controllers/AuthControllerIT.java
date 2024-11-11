package com.alten.dev.productsapp.it.controllers;

import com.alten.dev.productsapp.dto.UserLoginRequest;
import com.alten.dev.productsapp.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void testSuccessfulLogin() throws Exception {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin");

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("{\"username\":\"admin\",\"password\":\"admin\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void testInvalidLogin() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("{\"username\":\"wronguser\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testTokenExpiration() throws Exception {
        // Assuming the JWT token has a short expiration time
        String expiredToken = jwtUtil.generateToken("testuser"); // Token will expire immediately
        Thread.sleep(2000); // Wait for the token to expire

        mockMvc.perform(post("/protected-endpoint")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void testTokenValidation() throws Exception {
        String validToken = jwtUtil.generateToken("admin");

        mockMvc.perform(get("/api/products")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk());
    }

    @Test
    void testMissingToken() throws Exception {
        mockMvc.perform(post("/api/products"))
                .andExpect(status().isForbidden());
    }
}
