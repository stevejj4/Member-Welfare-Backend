package com.SUNData.MemberApp.Config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class for handling JWT (JSON Web Token) operations.
 * Responsibilities:
 * - Generate JWT tokens with username and role claims.
 * - Extract claims (username, role) from tokens.
 * - Validate tokens against signature and expiration.
 *
 * Security Notes:
 * - Uses HS256 algorithm with a strong secret key.
 * - Tokens include expiration to prevent indefinite validity.
 */
@Component
public class JwtUtil {

    private final String secret;
    private final long accessExpiration;
    private final long refreshExpiration;

    public JwtUtil(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-expiration-ms}") long accessExpiration,
            @Value("${app.jwt.refresh-expiration-ms}") long refreshExpiration) {
        this.secret = secret;
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    /**
     * Returns the signing key used for JWT operations.
     * Converts the secret string into a cryptographic key.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token containing username and role claims.
     *
     * @param username The user's email/username.
     * @param role     The user's role (ADMIN, FACILITATOR, COORDINATOR).
     * @return A signed JWT string.
     */
    public String generateToken(String username, String role) {
        return buildToken(username, role, accessExpiration);
    }

    public String generateRefreshToken(String username, String role) {
        return buildToken(username, role, refreshExpiration);
    }

    private String buildToken(String username, String role, long expirationMs) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username (subject) from a JWT token.
     *
     * @param token The JWT string.
     * @return The username/email stored in the token.
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Extracts the role claim from a JWT token.
     *
     * @param token The JWT string.
     * @return The role (e.g., ADMIN, FACILITATOR).
     */
    public String extractRole(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }

    /**
     * Validates a JWT token.
     * Checks signature and expiration.
     *
     * @param token The JWT string.
     * @return true if valid, false otherwise.
     *
     * -- Retrieves the custom "role" claim from the token body.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false; // invalid signature or expired token
        }
    }
}
