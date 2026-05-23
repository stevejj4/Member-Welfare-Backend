package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.Auth.LoginRequestDTO;
import com.SUNData.MemberApp.DTOs.Auth.AuthResponseDTO;
import com.SUNData.MemberApp.Config.JwtUtil;
import com.SUNData.MemberApp.Security.RolePermissionResolver;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import com.SUNData.MemberApp.Repository.SystemUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final SystemUserRepository systemUserRepository;
    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          SystemUserRepository systemUserRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.systemUserRepository = systemUserRepository;
    }
    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        SystemUserModel user = systemUserRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Use persisted role — not the first GrantedAuthority (may be a PBAC permission).
        String role = user.getRole().name();

        String token = jwtUtil.generateToken(
                userDetails.getUsername(),
                role
        );

        return new AuthResponseDTO(
                token,
                role,
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                RolePermissionResolver.permissionsFor(user.getRole())
        );
    }
}
