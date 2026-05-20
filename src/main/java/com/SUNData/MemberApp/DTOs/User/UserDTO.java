package com.SUNData.MemberApp.DTOs.User;

import com.SUNData.MemberApp.Enums.UserRole;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;

public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private UserRole role;

    public UserDTO() {}

    public UserDTO(SystemUserModel user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.role = user.getRole();
    }

    // Getters only (no password exposed)
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public UserRole getRole() { return role; }
}

