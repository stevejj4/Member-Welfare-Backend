package com.SUNData.MemberApp.DTOs.Auth;

import lombok.Data;
@Data
public class AuthResponseDTO {
    private String token;
    private String role;
    private Long id;
    private String email;
    private String fullName;
    private java.util.List<String> permissions;

    public AuthResponseDTO(String token, String role, Long id, String email, String fullName,
                           java.util.List<String> permissions) {
        this.token = token;
        this.role = role;
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.permissions = permissions;
    }

    public String getToken() { return token; }
    public String getRole() { return role; }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public java.util.List<String> getPermissions() { return permissions; }
}
