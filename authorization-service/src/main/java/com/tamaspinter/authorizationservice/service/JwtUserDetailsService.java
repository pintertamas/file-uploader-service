package com.tamaspinter.authorizationservice.service;

import com.tamaspinter.authorizationservice.model.User;
import com.tamaspinter.authorizationservice.proxy.UserProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserProxy userProxy;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseEntity<User> user = userProxy.findUserByUsername(username);

        if (user.getBody() == null || !user.getStatusCode().equals(HttpStatus.OK)) {
            throw new UsernameNotFoundException("User not found by: " + username);
        }

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        List<SimpleGrantedAuthority> authorities = List.of(simpleGrantedAuthority);

        return new org.springframework.security.core.userdetails.User(user.getBody().getUsername(), user.getBody().getPassword(), authorities);
    }
}
