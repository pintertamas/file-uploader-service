package com.tamaspinter.authorizationservice.component;

import com.tamaspinter.authorizationservice.proxy.UserProxy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtTokenUtil implements Serializable {

    final UserProxy userProxy;
    final JwtEncoder jwtEncoder;

    public static final int JWT_TOKEN_VALIDITY_IN_HOURS = 5;

    public JwtTokenUtil(UserProxy userProxy, JwtEncoder jwtEncoder) {
        this.userProxy = userProxy;
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(UserDetails userDetails, Long userId) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("FileUploaderService")
                .issuedAt(now)
                .expiresAt(now.plus(JWT_TOKEN_VALIDITY_IN_HOURS, ChronoUnit.HOURS))
                .subject(userDetails.getUsername())
                .claim("userId", userId.toString())
                .build();
        return jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}
