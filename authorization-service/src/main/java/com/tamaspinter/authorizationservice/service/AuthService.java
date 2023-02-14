package com.tamaspinter.authorizationservice.service;

import com.tamaspinter.authorizationservice.component.JwtTokenUtil;
import com.tamaspinter.authorizationservice.exception.UserNotFoundException;
import com.tamaspinter.authorizationservice.model.JwtRequest;
import com.tamaspinter.authorizationservice.model.User;
import com.tamaspinter.authorizationservice.proxy.UserProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    final UserProxy userProxy;
    private final JwtTokenUtil jwtTokenUtil;

    public String generateToken(JwtRequest authenticationRequest) throws BadCredentialsException, UserNotFoundException {
        User existingUser = getUserByUsername(authenticationRequest.getUsername());
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        return jwtTokenUtil.generateToken(userDetails, existingUser.getId());
    }

    private void authenticate(String username, String password) throws BadCredentialsException {
        try {
            log.info("Authenticating user: " + username + " with password: " + password);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new BadCredentialsException("BAD_CREDENTIALS");
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS");
        }
    }

    public User getUserByUsername(String username) throws UserNotFoundException {
        ResponseEntity<User> existingUser = userProxy.findUserByUsername(username);
        if (!existingUser.getStatusCode().equals(HttpStatus.OK)
                || existingUser.getBody() == null) {
            log.info("User not found");
            throw new UserNotFoundException(username);
        }
        return existingUser.getBody();
    }
}
