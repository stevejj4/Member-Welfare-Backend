package com.SUNData.MemberApp.Controller;

import com.SUNData.MemberApp.DTOs.User.CreateUserRequestDTO;
import com.SUNData.MemberApp.DTOs.User.UserDTO;
import com.SUNData.MemberApp.Enums.UserRole;
import com.SUNData.MemberApp.Service.auth.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AdminService adminService;

    public UserController(AdminService adminService) {
        this.adminService = adminService;
    }

    /** Create a new system user (Facilitator/Coordinator/Admin) */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserRequestDTO dto) {
        UserDTO user = adminService.createUser(dto);
        return ResponseEntity.ok(user);
    }

    /** Get all system users */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    /** Update a user’s role */
    @PatchMapping("/{id}/role")
    public ResponseEntity<UserDTO> updateUserRole(@PathVariable Long id,
                                                  @RequestParam UserRole role) {
        UserDTO updated = adminService.updateUserRole(id, role);
        return ResponseEntity.ok(updated);
    }

    /** Reset a user’s password */
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> resetPassword(@PathVariable Long id,
                                              @RequestParam String newPassword) {
        adminService.resetPassword(id, newPassword);
        return ResponseEntity.noContent().build();
    }
}
