package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.Auth.LoginRequestDTO;
import com.SUNData.MemberApp.DTOs.Auth.AuthResponseDTO;
import com.SUNData.MemberApp.DTOs.User.ForgotPasswordRequestDTO;
import com.SUNData.MemberApp.DTOs.User.RegisterUserDTO;
import com.SUNData.MemberApp.Config.JwtUtil;
import com.SUNData.MemberApp.DTOs.User.ResetPasswordRequestDTO;
import com.SUNData.MemberApp.Enums.UserRole;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import com.SUNData.MemberApp.Repository.SystemUserRepository;
import com.SUNData.MemberApp.Service.User.PasswordService;
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
    private final PasswordService passwordService; // inject PasswordService

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          SystemUserRepository systemUserRepository,
                          PasswordEncoder passwordEncoder,
                          PasswordService passwordService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.systemUserRepository = systemUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordService = passwordService;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String authority = userDetails.getAuthorities().iterator().next().getAuthority();
        String role = authority.replace("ROLE_", "");
        String token = jwtUtil.generateToken(userDetails.getUsername(), role);

        SystemUserModel user = systemUserRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthResponseDTO(token, role, user.getId(), user.getEmail(), user.getFullName());
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterUserDTO request) {
        SystemUserModel user = new SystemUserModel();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        systemUserRepository.save(user);
        return "User registered successfully";
    }

    // ---------------- PASSWORD ENDPOINTS ----------------

    @PostMapping("/password/forgot")
    public String forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        return passwordService.forgotPassword(request);
    }

    @PostMapping("/password/reset")
    public String resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        passwordService.resetPassword(request);
        return "Password successfully reset";
    }
}
