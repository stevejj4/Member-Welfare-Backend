package com.SUNData.MemberApp.DTOs.Auth;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private String role;

    public AuthResponseDTO(String token, String role) {
        this.token = token;
        this.role = role;
    }
}
