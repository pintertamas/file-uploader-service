package com.tamaspinter.sharingservice.service;

import com.tamaspinter.sharingservice.exception.UserNotFoundException;
import com.tamaspinter.sharingservice.model.User;
import com.tamaspinter.sharingservice.proxy.UserProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtUtil {

    final JwtDecoder jwtDecoder;
    final UserProxy userProxy;

    private String getTokenFromHeader(HttpHeaders headers) {
        return headers.getOrDefault("Authorization", new ArrayList<>()).get(0);
    }

    public User getUserFromHeader(HttpHeaders headers) throws UserNotFoundException {
        String token = getTokenFromHeader(headers);
        token = token.split(" ")[1].trim();
        String username = this.getAllClaimsFromToken(token).getOrDefault("sub", false).toString();
        ResponseEntity<User> user = userProxy.findUserByUsername(username);
        if (user.getBody() == null || !user.getStatusCode().equals(HttpStatus.OK)) {
            throw new UserNotFoundException(username);
        }
        return user.getBody();
    }

    public Long getUserIdFromHeader(HttpHeaders headers) throws UserNotFoundException {
        User user = getUserFromHeader(headers);
        return user.getId();
    }

    public Map<String, Object> getAllClaimsFromToken(String token) {
        try {
            return jwtDecoder.decode(token).getClaims();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("This token is not properly formatted");
        }
    }
}
