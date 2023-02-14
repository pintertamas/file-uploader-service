package com.tamaspinter.apigateway.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtil {

    private final JwtDecoder jwtDecoder;

    public Map<String, Object> getAllClaimsFromToken(String token) {
        try {
            token = token.split(" ")[1].trim(); // removes the "Bearer " substring from the beginning of the token
            return jwtDecoder.decode(token).getClaims();
        } catch (Exception e) {
            throw new IllegalArgumentException("This token is not properly formatted");
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Object expirationDateObject = this.getAllClaimsFromToken(token).getOrDefault("exp", false);
            log.info("exp: " + expirationDateObject);
            Instant expirationDate = Instant.parse(expirationDateObject.toString());
            Instant now = new Date().toInstant();
            boolean isExpired = expirationDate.compareTo(now) < 0;
            if (isExpired) throw new JwtException("This token is expired");
            return false;
        } catch (Exception e) {
            log.error(e.getMessage());
            return true;
        }
    }

}