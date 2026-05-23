package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.User.ForgotPasswordRequestDTO;
import com.SUNData.MemberApp.DTOs.User.MessageResponseDTO;
import com.SUNData.MemberApp.DTOs.User.ResetPasswordRequestDTO;
import com.SUNData.MemberApp.Service.User.PasswordService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthV1Controller {

    private final PasswordService passwordService;

    public AuthV1Controller(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponseDTO> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDTO request) {
        return ResponseEntity.ok(passwordService.requestPasswordReset(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponseDTO> resetPassword(
            @Valid @RequestBody ResetPasswordRequestDTO request) {
        return ResponseEntity.ok(passwordService.resetPasswordWithOtp(request));
    }
}
