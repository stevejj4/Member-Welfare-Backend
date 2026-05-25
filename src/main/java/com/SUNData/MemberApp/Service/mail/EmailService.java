package com.SUNData.MemberApp.Service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * EmailService handles all outgoing email operations for the application.
 * - Uses Thymeleaf templates to generate dynamic HTML content.
 * - Sends emails asynchronously to avoid blocking request threads.
 * - Provides specialized methods for welcome credentials and password reset OTPs.
 * - Centralizes error handling and logging for email dispatch.
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;              // Spring’s mail sender abstraction
    private final SpringTemplateEngine mailTemplateEngine; // Thymeleaf engine for rendering HTML templates

    // Configurable properties injected from application.properties or environment variables
    @Value("${app.mail.from:}")
    private String fromAddress;   // Preferred sender address (overrides username if set)

    @Value("${spring.mail.username:}")
    private String mailUsername;  // SMTP username (fallback sender if fromAddress is blank)

    @Value("${app.mail.enabled:true}")
    private boolean mailEnabled;  // Toggle to enable/disable email sending (useful for dev/test)

    @Value("${app.name:SUN Welfare}")
    private String appName;       // Application name (used in subjects and templates)

    @Value("${app.frontend.login-url:http://localhost:5173/login}")
    private String loginUrl;      // Frontend login URL (embedded in welcome emails)

    /**
     * Constructor injection ensures EmailService has required dependencies.
     * @param mailSender JavaMailSender configured with SMTP settings
     * @param mailTemplateEngine Thymeleaf engine for processing templates
     */
    public EmailService(JavaMailSender mailSender, SpringTemplateEngine mailTemplateEngine) {
        this.mailSender = mailSender;
        this.mailTemplateEngine = mailTemplateEngine;
    }

    /**
     * Sends a welcome email with temporary credentials to a new user.
     * Runs asynchronously so the main request thread is not blocked.
     *
     * @param email recipient email address
     * @param fullName recipient full name
     * @param roleLabel assigned role (e.g., Facilitator, Coordinator)
     * @param temporaryPassword generated temporary password
     */
    @Async
    public void sendWelcomeCredentials(String email, String fullName, String roleLabel, String temporaryPassword) {
        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("full_name", fullName); // duplicate key for template flexibility
        context.setVariable("email", email);
        context.setVariable("roleLabel", roleLabel);
        context.setVariable("temporaryPassword", temporaryPassword);
        context.setVariable("loginUrl", loginUrl);
        context.setVariable("appName", appName);

        try {
            // Render HTML from Thymeleaf template
            String html = mailTemplateEngine.process("welcome-credentials", context);
            // Dispatch email
            dispatchHtml(email, appName + " — Your account credentials", html);
        } catch (Exception ex) {
            log.error("Failed to prepare welcome email for {}: {}", email, ex.getMessage(), ex);
        }
    }

    /**
     * Sends a password reset OTP email to a user.
     * Runs asynchronously so the main request thread is not blocked.
     *
     * @param email recipient email address
     * @param otpCode generated OTP code
     * @param expiryMinutes validity period of the OTP in minutes
     */
    @Async
    public void sendPasswordResetOtp(String email, String otpCode, int expiryMinutes) {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("otpCode", otpCode);
        context.setVariable("expiryMinutes", expiryMinutes);
        context.setVariable("appName", appName);

        try {
            // Render HTML from Thymeleaf template
            String html = mailTemplateEngine.process("password-reset", context);
            // Dispatch email
            dispatchHtml(email, appName + " — Password reset verification code", html);
        } catch (Exception ex) {
            log.error("Failed to prepare password-reset email for {}: {}", email, ex.getMessage(), ex);
        }
    }

    /**
     * Core method that actually sends the email via SMTP.
     * Handles validation, sender resolution, and error logging.
     *
     * @param to recipient email address
     * @param subject email subject line
     * @param htmlBody rendered HTML content
     */
    private void dispatchHtml(String to, String subject, String htmlBody) {
        // Skip sending if disabled
        if (!mailEnabled) {
            log.warn("Email dispatch skipped (app.mail.enabled=false). Recipient={}, subject={}", to, subject);
            return;
        }

        // Validate recipient
        if (!StringUtils.hasText(to)) {
            log.error("Email dispatch aborted: recipient address is blank. subject={}", subject);
            return;
        }

        // Resolve sender address
        String sender = resolveFromAddress();
        if (!StringUtils.hasText(sender)) {
            log.error("Email dispatch aborted: no sender configured. Set SMTP_USERNAME or app.mail.from. recipient={}", to);
            return;
        }

        try {
            // Build MIME message with HTML content
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML content

            // Send via SMTP
            mailSender.send(message);
            log.info("Email successfully sent to {} — subject={}", to, subject);
        } catch (MessagingException ex) {
            log.error("MessagingException while sending email to {} — subject={}: {}", to, subject, ex.getMessage(), ex);
        } catch (MailException ex) {
            log.error("MailException while sending email to {} — subject={}: {}", to, subject, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Unexpected error while sending email to {} — subject={}: {}", to, subject, ex.getMessage(), ex);
        }
    }

    /**
     * Resolves the sender address.
     * - Uses app.mail.from if configured.
     * - Falls back to spring.mail.username otherwise.
     *
     * @return sender email address
     */
    private String resolveFromAddress() {
        if (StringUtils.hasText(fromAddress)) {
            return fromAddress;
        }
        return mailUsername;
    }
}
