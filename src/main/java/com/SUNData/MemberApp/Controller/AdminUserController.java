package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.User.*;
import com.SUNData.MemberApp.Enums.UserRole;
import com.SUNData.MemberApp.Service.admin.AdminUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER_MANAGEMENT')")
    public ResponseEntity<CreateUserResponseDTO> createUser(
            @Valid @RequestBody CreateUserRequestDTO request) {
        CreateUserResponseDTO response = adminUserService.provisionUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/reset-password")
    public ResponseEntity<MessageResponseDTO> resetPassword(
            @PathVariable Long id,
            @Valid @RequestBody AdminResetPasswordRequestDTO request) {
        adminUserService.adminResetPassword(id, request.getNewPassword());
        return ResponseEntity.ok(new MessageResponseDTO("Password updated successfully."));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_MANAGEMENT')")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequestDTO request) {
        return ResponseEntity.ok(adminUserService.updateUser(request, id));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<UserDTO> updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRoleRequestDTO request) {
        return ResponseEntity.ok(adminUserService.updateUserRole(id, request.getRole()));
    }
}
