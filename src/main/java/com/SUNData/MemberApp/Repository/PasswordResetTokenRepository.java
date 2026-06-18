package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.UserModel.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link PasswordResetToken} entities.
 * <p>
 * Provides CRUD operations and custom query methods for handling password
 * reset tokens associated with system users. This repository supports
 * token lifecycle management, including creation, validation, and cleanup.
 */
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    /**
     * Deletes all password reset tokens associated with a specific user.
     * <p>
     * Useful for invalidating tokens when a user resets their password
     * or when tokens need to be cleared for security reasons.
     *
     * @param userId the unique identifier of the user
     */
    void deleteByUserId(Long userId);

    /**
     * Retrieves all active (non-expired) password reset tokens for a given user.
     * <p>
     * This method checks tokens against the provided timestamp to ensure
     * only valid tokens are returned.
     *
     * @param userId the unique identifier of the user
     * @param now    the current timestamp used to filter expired tokens
     * @return list of valid password reset tokens for the user
     */
    List<PasswordResetToken> findByUserIdAndExpiresAtAfter(Long userId, LocalDateTime now);

    /**
     * Retrieves the most recently created active (non-expired) password reset token
     * for a given user.
     * <p>
     * This is useful when enforcing a single valid token per user or when
     * prioritizing the latest token for validation.
     *
     * @param userId the unique identifier of the user
     * @param now    the current timestamp used to filter expired tokens
     * @return an {@link Optional} containing the latest valid token if present,
     *         or empty if no valid token exists
     */
    Optional<PasswordResetToken> findFirstByUserIdAndExpiresAtAfterOrderByCreatedAtDesc(
            Long userId,
            LocalDateTime now
    );
}
