package com.alten.dev.productsapp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secretKey}")
    private String SECRET_KEY; // Utilisez une clé sécurisée et forte

    // Génère un token JWT
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ROLE_USER");
        return createToken(claims, username);
    }

    // Crée un token avec les informations et la date d'expiration
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 heures
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Valide le token en comparant le nom d'utilisateur et en vérifiant l'expiration
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    // Extrait le nom d'utilisateur (subject) du token JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Vérifie si le token est expiré
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extrait la date d'expiration
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Méthode générique pour extraire un claim spécifique
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    //Generer une cle secrete

    /*public static String generateSecretKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] secretKeyBytes = new byte[32];  // 256-bit key (32 bytes)
        secureRandom.nextBytes(secretKeyBytes);  // Fill the byte array with random bytes
        return Base64.getEncoder().encodeToString(secretKeyBytes);  // Encode the byte array to Base64 string
    }

    public static void main(String[] args) {
        String secretKey = generateSecretKey();
        System.out.println("Generated Secret Key: " + secretKey);
    }*/
}
