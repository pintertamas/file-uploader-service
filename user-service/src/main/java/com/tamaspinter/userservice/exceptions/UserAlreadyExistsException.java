package com.tamaspinter.userservice.exceptions;

import com.tamaspinter.userservice.model.User;

public class UserAlreadyExistsException extends Exception {
    private final transient User user;

    public UserAlreadyExistsException(User user) {
        super(user.getUsername() + " is a taken username");
        this.user = user;
    }

    public String getUsername() {
        return user.getUsername();
    }
}
