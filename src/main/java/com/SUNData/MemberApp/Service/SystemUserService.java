package com.SUNData.MemberApp.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.SUNData.MemberApp.DTOs.User.RegisterUserDTO;
import com.SUNData.MemberApp.Exceptions.ValidationException;
import com.SUNData.MemberApp.Model.UserModel.Role;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import com.SUNData.MemberApp.Repository.SystemUserRepository;

@Service
public class SystemUserService {

    private final SystemUserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public SystemUserService(SystemUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public SystemUserModel registerUser(RegisterUserDTO dto) {
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new ValidationException("Email already registered");
        }
        SystemUserModel user = new SystemUserModel();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // hash password

        String roleStr = dto.getRole() != null ? dto.getRole().trim().toUpperCase() : "FACILITATOR";
        Role role;
        try {
            role = Role.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            role = Role.FACILITATOR;
        }
        user.setRole(role);

        return userRepo.save(user);
    }

    public SystemUserModel login(String email, String rawPassword) {
        SystemUserModel user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Invalid email or password"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new ValidationException("Invalid email or password");
        }
        return user;
    }
}
