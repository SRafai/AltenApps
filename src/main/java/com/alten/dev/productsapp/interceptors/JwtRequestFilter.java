package com.alten.dev.productsapp.interceptors;

import com.alten.dev.productsapp.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String tokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        // Check if the Authorization header is present and starts with "Bearer "
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            jwtToken = tokenHeader.substring(7); // Extract token
            username = jwtUtil.extractUsername(jwtToken); // Extract username from token
        }

        // If there's a username in the token and there's no existing authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Validate the token
            if (jwtUtil.validateToken(jwtToken, username)) {
                // Extract roles from the token
                List<GrantedAuthority> authorities = getAuthorities(jwtToken); // Get roles

                // If the token is valid, create an authentication token and set it in the SecurityContext
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);  // Include authorities
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Set the authentication in the context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continue with the next filter in the filter chain
        filterChain.doFilter(request, response);
    }

    private List<GrantedAuthority> getAuthorities(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        // Get the roles from the claims and create a list of GrantedAuthority objects
        String role = claims.get("role", String.class); // Ensure the claim key matches
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

}
