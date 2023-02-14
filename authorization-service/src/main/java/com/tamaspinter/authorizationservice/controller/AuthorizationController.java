package com.tamaspinter.authorizationservice.controller;

import com.tamaspinter.authorizationservice.exception.UserNotFoundException;
import com.tamaspinter.authorizationservice.model.JwtRequest;
import com.tamaspinter.authorizationservice.model.User;
import com.tamaspinter.authorizationservice.model.UserResponse;
import com.tamaspinter.authorizationservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthorizationController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> createAuthenticationToken(
            @RequestBody JwtRequest authRequest) {
        try {
            log.info("Authenticating user with credentials: " + authRequest.toString());
            String token = authService.generateToken(authRequest);
            User user = authService.getUserByUsername(authRequest.getUsername());
            log.info("NEW LOGIN");
            return new ResponseEntity<>(new UserResponse(user, token), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.error("USER NOT FOUND");
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (BadCredentialsException e) {
            log.error("BAD CREDENTIALS");
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("ERROR AT LOGIN");
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
