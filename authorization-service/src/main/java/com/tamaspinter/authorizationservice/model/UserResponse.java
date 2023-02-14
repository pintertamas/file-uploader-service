package com.tamaspinter.authorizationservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String username;
    private String jwtToken;

    public UserResponse(User user, String token) {
        this.setUserId(user.getId());
        this.setUsername(user.getUsername());
        this.setJwtToken(token);
    }
}
