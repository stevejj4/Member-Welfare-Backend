package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.User.CreateUserRequestDTO;
import com.SUNData.MemberApp.DTOs.User.LoginRequestDTO;
import com.SUNData.MemberApp.DTOs.User.UserDTO;
import com.SUNData.MemberApp.Service.auth.AdminService;
import com.SUNData.MemberApp.Service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AdminService adminService;

    public AuthController(AuthService authService, AdminService adminService) {
        this.authService = authService;
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody CreateUserRequestDTO dto) {
        UserDTO user = adminService.createUser(dto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        UserDTO user = authService.login(dto);
        return ResponseEntity.ok(user);
    }
}
