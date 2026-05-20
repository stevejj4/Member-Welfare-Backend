package com.SUNData.MemberApp.DTOs.Auth;

import lombok.Data;
@Data
public class AuthResponseDTO {
    private String token;
    private String role;
    private Long id;
    private String email;
    private String fullName;

    // constructor
    public AuthResponseDTO(String token, String role, Long id, String email, String fullName) {
        this.token = token;
        this.role = role;
        this.id = id;
        this.email = email;
        this.fullName = fullName;
    }

    // getters & setters
    public String getToken() { return token; }
    public String getRole() { return role; }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
}
