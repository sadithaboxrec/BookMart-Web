package com.sadi.bookshop.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class CookieService {

    public static final String COOKIE_NAME = "auth_token";

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    public void addJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setHttpOnly(true);       // JS cannot access it
        cookie.setSecure(false);        // set true in production (HTTPS)
        cookie.setPath("/");
        cookie.setMaxAge((int) (expirationMs / 1000));
        // cookie.setAttribute("SameSite", "Strict"); // add in prod
        response.addCookie(cookie);
    }

    public Optional<String> extractJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();
        return Arrays.stream(request.getCookies())
                .filter(c -> COOKIE_NAME.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    public void clearJwtCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);  // deletes it
        response.addCookie(cookie);
    }
}