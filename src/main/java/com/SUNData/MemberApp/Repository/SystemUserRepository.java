package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface SystemUserRepository extends JpaRepository<SystemUserModel, Long> {

    // Find a user by email (used in login/auth flows)
    Optional<SystemUserModel> findByEmail(String email);

    // Check if a user exists by email (used in account creation to enforce uniqueness)
    boolean existsByEmail(String email);

    // Optional: find all users by role (useful for admin dashboards)
    List<SystemUserModel> findByRole(com.SUNData.MemberApp.Enums.UserRole role);

    // Optional: search by full name (for admin lookups)
    List<SystemUserModel> findByFullNameContainingIgnoreCase(String fullName);

    Optional<SystemUserModel> findByResetToken(String token);

}
