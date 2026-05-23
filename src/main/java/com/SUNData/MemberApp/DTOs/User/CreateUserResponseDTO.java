package com.SUNData.MemberApp.DTOs.User;

public class CreateUserResponseDTO {

    private UserDTO user;
    private String message;

    public CreateUserResponseDTO(UserDTO user, String message) {
        this.user = user;
        this.message = message;
    }

    public UserDTO getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
