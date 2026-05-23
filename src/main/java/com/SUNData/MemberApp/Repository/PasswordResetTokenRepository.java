package com.SUNData.MemberApp.Repository;

import com.SUNData.MemberApp.Model.UserModel.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    void deleteByUserId(Long userId);

    List<PasswordResetToken> findByUserIdAndExpiresAtAfter(Long userId, LocalDateTime now);

    Optional<PasswordResetToken> findFirstByUserIdAndExpiresAtAfterOrderByCreatedAtDesc(
            Long userId,
            LocalDateTime now
    );
}
