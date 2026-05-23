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

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine mailTemplateEngine;

    @Value("${app.mail.from:}")
    private String fromAddress;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @Value("${app.mail.enabled:true}")
    private boolean mailEnabled;

    @Value("${app.name:SUN Welfare}")
    private String appName;

    @Value("${app.frontend.login-url:http://localhost:5173/login}")
    private String loginUrl;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine mailTemplateEngine) {
        this.mailSender = mailSender;
        this.mailTemplateEngine = mailTemplateEngine;
    }

    @Async
    public void sendWelcomeCredentials(String email, String fullName, String roleLabel, String temporaryPassword) {
        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("full_name", fullName);
        context.setVariable("email", email);
        context.setVariable("roleLabel", roleLabel);
        context.setVariable("temporaryPassword", temporaryPassword);
        context.setVariable("loginUrl", loginUrl);
        context.setVariable("appName", appName);

        try {
            String html = mailTemplateEngine.process("welcome-credentials", context);
            dispatchHtml(email, appName + " — Your account credentials", html);
        } catch (Exception ex) {
            log.error("Failed to prepare welcome email for {}: {}", email, ex.getMessage(), ex);
        }
    }

    @Async
    public void sendPasswordResetOtp(String email, String otpCode, int expiryMinutes) {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("otpCode", otpCode);
        context.setVariable("expiryMinutes", expiryMinutes);
        context.setVariable("appName", appName);

        try {
            String html = mailTemplateEngine.process("password-reset", context);
            dispatchHtml(email, appName + " — Password reset verification code", html);
        } catch (Exception ex) {
            log.error("Failed to prepare password-reset email for {}: {}", email, ex.getMessage(), ex);
        }
    }

    private void dispatchHtml(String to, String subject, String htmlBody) {
        if (!mailEnabled) {
            log.warn("Email dispatch skipped (app.mail.enabled=false). Recipient={}, subject={}", to, subject);
            return;
        }

        if (!StringUtils.hasText(to)) {
            log.error("Email dispatch aborted: recipient address is blank. subject={}", subject);
            return;
        }

        String sender = resolveFromAddress();
        if (!StringUtils.hasText(sender)) {
            log.error(
                    "Email dispatch aborted: no sender configured. Set SMTP_USERNAME or app.mail.from. recipient={}",
                    to
            );
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
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

    private String resolveFromAddress() {
        if (StringUtils.hasText(fromAddress)) {
            return fromAddress;
        }
        return mailUsername;
    }
}
