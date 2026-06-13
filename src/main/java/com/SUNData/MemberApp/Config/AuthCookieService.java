package com.SUNData.MemberApp.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class AuthCookieService {

    public static final String REFRESH_COOKIE_NAME = "refresh_token";

    @Value("${app.auth.cookie.secure:false}")
    private boolean cookieSecure;

    @Value("${app.auth.cookie.same-site:Lax}")
    private String cookieSameSite;

    public ResponseCookie buildRefreshCookie(String value, long maxAgeSeconds) {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, value)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .path("/api")
                .maxAge(maxAgeSeconds)
                .build();
    }

    public ResponseCookie clearRefreshCookie() {
        return buildRefreshCookie("", 0);
    }
}
