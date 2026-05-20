package com.SUNData.MemberApp.Service.User;

import com.SUNData.MemberApp.DTOs.Auth.LoginRequestDTO;
import com.SUNData.MemberApp.DTOs.User.UserDTO;
import com.SUNData.MemberApp.Exceptions.ResourceNotFoundException;
import com.SUNData.MemberApp.Exceptions.ValidationException;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import com.SUNData.MemberApp.Repository.SystemUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final SystemUserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(SystemUserRepository userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Login method: validates email and password.
     * Returns a UserDTO if successful, otherwise throws exceptions.
     */
    public UserDTO login(LoginRequestDTO request) {
        // Find user by email
        SystemUserModel user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ValidationException("Invalid credentials");
        }

        // Normally you’d issue a JWT or session token here
        return new UserDTO(user);
    }
}
