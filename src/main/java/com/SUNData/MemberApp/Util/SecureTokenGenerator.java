package com.SUNData.MemberApp.Util;

import java.security.SecureRandom;

public final class SecureTokenGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String TEMP_PASSWORD_CHARS =
            "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%";

    private SecureTokenGenerator() {
    }

    /** Generates a cryptographically secure temporary password (16 chars). */
    public static String generateTemporaryPassword() {
        return randomString(TEMP_PASSWORD_CHARS, 16);
    }

    /** Generates a 6-digit numeric OTP for password reset. */
    public static String generateSixDigitOtp() {
        int code = RANDOM.nextInt(900_000) + 100_000;
        return String.valueOf(code);
    }

    private static String randomString(String alphabet, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(RANDOM.nextInt(alphabet.length())));
        }
        return sb.toString();
    }
}
