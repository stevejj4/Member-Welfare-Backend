package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.Auth.LoginRequestDTO;
import com.SUNData.MemberApp.DTOs.Auth.AuthResponseDTO;
import com.SUNData.MemberApp.DTOs.User.RegisterUserDTO;
import com.SUNData.MemberApp.Config.JwtUtil;
import com.SUNData.MemberApp.Enums.UserRole;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import com.SUNData.MemberApp.Repository.SystemUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final SystemUserRepository systemUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          SystemUserRepository systemUserRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.systemUserRepository = systemUserRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        String token = jwtUtil.generateToken(userDetails.getUsername(), role);

        SystemUserModel user = systemUserRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthResponseDTO(token, role, user.getId(), user.getEmail(), user.getFullName());
    }


    // 🔹 REGISTER ENDPOINT
    @PostMapping("/register")
    public String register(@RequestBody RegisterUserDTO request) {
        SystemUserModel user = new SystemUserModel();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // BCrypt encoding
        user.setRole(UserRole.valueOf(request.getRole().toString())); // convert string to enum

        systemUserRepository.save(user);
        return "User registered successfully";
    }
}
