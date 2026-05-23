package com.SUNData.MemberApp.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.SUNData.MemberApp.Security.RolePermissionResolver;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * ==============================================================
 * JWT AUTHENTICATION FILTER
 * ==============================================================
 *
 * PURPOSE:
 * This filter intercepts every HTTP request once per request
 * and performs JWT-based authentication.
 *
 * RESPONSIBILITIES:
 * 1. Extract JWT token from Authorization header
 * 2. Validate token integrity and expiration
 * 3. Extract user identity (username + role) from token
 * 4. Convert role into Spring Security authorities
 * 5. Set authentication into SecurityContext
 *
 * WHY THIS EXISTS:
 * - Enables stateless authentication (no session storage)
 * - Ensures every request is securely validated
 * - Integrates JWT with Spring Security authorization system
 *
 * SECURITY MODEL:
 * - JWT is trusted after validation (signature + expiry)
 * - Role is derived from token claims
 * - Spring Security uses ROLE_ prefix convention
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Main filter execution method.
     * <p>
     * Flow:
     * 1. Read Authorization header
     * 2. Extract Bearer token
     * 3. Validate token
     * 4. Extract username and role
     * 5. Normalize role format
     * 6. Create Authentication object
     * 7. Store in SecurityContext
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token != null
                && jwtUtil.validateToken(token)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            String username = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);

            // ================================
            // VALIDATION (IMPORTANT)
            // ================================
            if (role == null || role.isBlank()) {
                throw new RuntimeException("JWT role is missing");
            }

            // ================================
            // NORMALIZATION
            // ================================
            if (role.startsWith("ROLE_")) {
                role = role.substring(5);
            }

            role = role.toUpperCase();

            // ================================
            // PBAC: role + fine-grained permissions
            // ================================
            List<GrantedAuthority> authorities = new java.util.ArrayList<>(
                    RolePermissionResolver.authoritiesForRoleName(role)
            );

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            authorities
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}