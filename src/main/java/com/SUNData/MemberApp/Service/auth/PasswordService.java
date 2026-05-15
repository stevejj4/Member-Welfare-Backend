package com.SUNData.MemberApp.Service.auth;

import com.SUNData.MemberApp.DTOs.User.ForgotPasswordRequestDTO;
import com.SUNData.MemberApp.DTOs.User.ResetPasswordRequestDTO;
import com.SUNData.MemberApp.Exceptions.ResourceNotFoundException;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import com.SUNData.MemberApp.Repository.SystemUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordService {

    private final SystemUserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordService(SystemUserRepository userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Forgot password: generate a reset token and send via email.
     */
    public String forgotPassword(ForgotPasswordRequestDTO request) {
        SystemUserModel user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        // Generate token
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepo.save(user);

        // Here you’d send the token via email (SMTP integration)
        System.out.println("Password reset token sent to " + user.getEmail() + ": " + token);

        return token;
    }

    /**
     * Reset password using token.
     */
    public void resetPassword(ResetPasswordRequestDTO request) {
        SystemUserModel user = userRepo.findByResetToken(request.getToken())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired reset token"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null); // clear token after use
        userRepo.save(user);
    }
}
