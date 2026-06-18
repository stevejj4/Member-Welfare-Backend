package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

/**
 * Repository interface for managing {@link SystemUserModel} entities.
 * <p>
 * Provides CRUD operations and custom query methods for system users.
 * This repository supports authentication flows, uniqueness checks,
 * role-based queries, and password reset token lookups.
 */
public interface SystemUserRepository extends JpaRepository<SystemUserModel, Long> {

    /**
     * Finds a system user by their email address.
     * <p>
     * Commonly used in login and authentication flows.
     *
     * @param email the email address of the user
     * @return an {@link Optional} containing the user if found,
     *         or empty if no user exists with the given email
     */
    Optional<SystemUserModel> findByEmail(String email);

    /**
     * Checks if a system user exists with the given email address.
     * <p>
     * Useful for enforcing uniqueness during account creation.
     *
     * @param email the email address to check
     * @return true if a user exists with the given email, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Retrieves all system users with a specific role.
     * <p>
     * Useful for admin dashboards or role-based queries.
     *
     * @param role the role of the users (e.g., ADMIN, FACILITATOR)
     * @return list of users with the given role
     */
    List<SystemUserModel> findByRole(com.SUNData.MemberApp.Enums.UserRole role);

    /**
     * Searches for system users whose full name contains the given string,
     * ignoring case sensitivity.
     * <p>
     * Useful for admin lookups or flexible search functionality.
     *
     * @param fullName the name fragment to search for
     * @return list of users whose full name contains the given string
     */
    List<SystemUserModel> findByFullNameContainingIgnoreCase(String fullName);

    /**
     * Finds a system user by their password reset token.
     * <p>
     * Useful for password reset workflows to validate and apply tokens.
     *
     * @param token the reset token string
     * @return an {@link Optional} containing the user if found,
     *         or empty if no user exists with the given token
     */
    Optional<SystemUserModel> findByResetToken(String token);
}
