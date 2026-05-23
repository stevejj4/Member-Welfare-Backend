package com.SUNData.MemberApp.Service.User;

import com.SUNData.MemberApp.DTOs.User.ForgotPasswordRequestDTO;
import com.SUNData.MemberApp.DTOs.User.MessageResponseDTO;
import com.SUNData.MemberApp.DTOs.User.ResetPasswordRequestDTO;
import com.SUNData.MemberApp.Exceptions.ValidationException;
import com.SUNData.MemberApp.Model.UserModel.PasswordResetToken;
import com.SUNData.MemberApp.Model.UserModel.SystemUserModel;
import com.SUNData.MemberApp.Repository.PasswordResetTokenRepository;
import com.SUNData.MemberApp.Repository.SystemUserRepository;
import com.SUNData.MemberApp.Service.mail.EmailService;
import com.SUNData.MemberApp.Util.SecureTokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordService {

    private static final Logger log = LoggerFactory.getLogger(PasswordService.class);
    private static final int OTP_EXPIRY_MINUTES = 15;
    private static final String FORGOT_PASSWORD_GENERIC_MESSAGE =
            "If an account exists for that email, a verification code has been sent.";

    private final SystemUserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PasswordService(
            SystemUserRepository userRepository,
            PasswordResetTokenRepository tokenRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /**
     * Always returns a generic message to prevent email enumeration.
     */
    @Transactional
    public MessageResponseDTO requestPasswordReset(ForgotPasswordRequestDTO request) {
        String email = request.getEmail().trim().toLowerCase();
        Optional<SystemUserModel> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            SystemUserModel user = userOpt.get();
            String otp = SecureTokenGenerator.generateSixDigitOtp();

            tokenRepository.deleteByUserId(user.getId());

            PasswordResetToken token = new PasswordResetToken();
            token.setUserId(user.getId());
            token.setCodeHash(passwordEncoder.encode(otp));
            token.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
            tokenRepository.save(token);

            emailService.sendPasswordResetOtp(email, otp, OTP_EXPIRY_MINUTES);
            log.info("Password reset OTP issued for user id={}", user.getId());
        } else {
            log.info("Password reset requested for unknown email (no enumeration)");
        }

        return new MessageResponseDTO(FORGOT_PASSWORD_GENERIC_MESSAGE);
    }

    @Transactional
    public MessageResponseDTO resetPasswordWithOtp(ResetPasswordRequestDTO request) {
        String email = request.getEmail().trim().toLowerCase();
        SystemUserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Invalid verification code or email"));

        PasswordResetToken token = tokenRepository
                .findFirstByUserIdAndExpiresAtAfterOrderByCreatedAtDesc(user.getId(), LocalDateTime.now())
                .orElseThrow(() -> new ValidationException("Invalid or expired verification code"));

        if (!passwordEncoder.matches(request.getCode().trim(), token.getCodeHash())) {
            throw new ValidationException("Invalid or expired verification code");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setMustChangePassword(false);
        user.setResetToken(null);
        userRepository.save(user);

        tokenRepository.deleteByUserId(user.getId());

        log.info("Password reset completed for user id={}", user.getId());
        return new MessageResponseDTO("Password successfully reset. You may now sign in.");
    }
}
