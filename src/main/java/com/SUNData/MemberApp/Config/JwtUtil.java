package com.SUNData.MemberApp.Config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

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

    // 🔹 Secret key for signing JWTs (must be at least 256 bits for HS256).
    // In production, store this securely (e.g., environment variable).
    private static final String SECRET = "mySuperSecretKeyForJwtGeneration1234567890";

    // Access token expiration (15 minutes).
    private static final long ACCESS_EXPIRATION = 1000 * 60 * 15;

    // Refresh token expiration (7 days).
    private static final long REFRESH_EXPIRATION = 1000L * 60 * 60 * 24 * 7;

    /**
     * Returns the signing key used for JWT operations.
     * Converts the secret string into a cryptographic key.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token containing username and role claims.
     *
     * @param username The user's email/username.
     * @param role     The user's role (ADMIN, FACILITATOR, COORDINATOR).
     * @return A signed JWT string.
     */
    public String generateToken(String username, String role) {
        return buildToken(username, role, ACCESS_EXPIRATION);
    }

    public String generateRefreshToken(String username, String role) {
        return buildToken(username, role, REFRESH_EXPIRATION);
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
