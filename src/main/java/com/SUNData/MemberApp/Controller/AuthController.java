package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.Config.AuthCookieService;
import com.SUNData.MemberApp.Config.JwtUtil;
import com.SUNData.MemberApp.DTOs.Auth.AuthResponseDTO;
import com.SUNData.MemberApp.DTOs.Auth.LoginRequestDTO;
import com.SUNData.MemberApp.DTOs.Auth.TokenRefreshResponseDTO;
import com.SUNData.MemberApp.Model.LocationModel.WardModel;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import com.SUNData.MemberApp.Repository.SystemUserRepository;
import com.SUNData.MemberApp.Security.RolePermissionResolver;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final long REFRESH_COOKIE_MAX_AGE_SECONDS = 60L * 60 * 24 * 7;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final SystemUserRepository systemUserRepository;
    private final AuthCookieService authCookieService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          SystemUserRepository systemUserRepository,
                          AuthCookieService authCookieService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.systemUserRepository = systemUserRepository;
        this.authCookieService = authCookieService;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request,
                                 HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        SystemUserModel user = systemUserRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String role = user.getRole().name();
        String username = userDetails.getUsername();

        String accessToken = jwtUtil.generateToken(username, role);
        String refreshToken = jwtUtil.generateRefreshToken(username, role);

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                authCookieService
                        .buildRefreshCookie(refreshToken, REFRESH_COOKIE_MAX_AGE_SECONDS)
                        .toString()
        );

        return new AuthResponseDTO(
                accessToken,
                role,
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                RolePermissionResolver.permissionsFor(user.getRole()),
                user.getAssignedCounty() != null ? user.getAssignedCounty().getId() : null,
                user.getAssignedCounty() != null ? user.getAssignedCounty().getName() : null,
                user.getAssignedSubCounty() != null ? user.getAssignedSubCounty().getId() : null,
                user.getAssignedSubCounty() != null ? user.getAssignedSubCounty().getName() : null,
                user.getAssignedWards().stream().map(WardModel::getId).toList(),
                user.getAssignedWards().stream().map(WardModel::getName).toList()
        );
    }

    @PostMapping("/refresh")
    public TokenRefreshResponseDTO refresh(HttpServletRequest request) {
        String refreshToken = extractRefreshToken(request);

        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        String role = jwtUtil.extractRole(refreshToken);

        if (role == null || role.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token claims");
        }

        String accessToken = jwtUtil.generateToken(username, role);
        return new TokenRefreshResponseDTO(accessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                authCookieService.clearRefreshCookie().toString()
        );
        return ResponseEntity.noContent().build();
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (AuthCookieService.REFRESH_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
