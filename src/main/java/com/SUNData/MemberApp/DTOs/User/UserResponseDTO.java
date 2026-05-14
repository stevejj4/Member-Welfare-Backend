package com.SUNData.MemberApp.DTOs.User;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String email;
    private String fullName;
    private String role;

    public UserResponseDTO(Long id, String email, String fullName, String role) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }

    // getters...

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }
}
