package com.SUNData.MemberApp.DTOs.Auth;

// token refresh happens at the server
public class TokenRefreshResponseDTO {
    private String token;

    public TokenRefreshResponseDTO() {
    }

    public TokenRefreshResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
