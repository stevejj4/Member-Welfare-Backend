package com.SUNData.MemberApp.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SUNData.MemberApp.DTOs.User.LoginDTO;
import com.SUNData.MemberApp.DTOs.User.RegisterUserDTO;
import com.SUNData.MemberApp.DTOs.User.UserResponseDTO;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import com.SUNData.MemberApp.Service.SystemUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SystemUserService userService;

    public AuthController(SystemUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterUserDTO dto) {
        SystemUserModel user = userService.registerUser(dto);
        return ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getEmail(), user.getFullName(), user.getRole().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
        SystemUserModel user = userService.login(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getEmail(), user.getFullName(), user.getRole().name()));
    }
}
