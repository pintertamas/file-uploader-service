package com.tamaspinter.userservice.dto;

import com.tamaspinter.userservice.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    String username;
    String password;

    public User userFromDTO() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    public User userFromDTOWithId(Long id) {
        User user = userFromDTO();
        user.setId(id);
        return user;
    }
}
