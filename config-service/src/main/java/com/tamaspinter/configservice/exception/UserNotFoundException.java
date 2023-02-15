package com.tamaspinter.configservice.exception;

public class UserNotFoundException extends Exception {
    private static final String USER_NOT_FOUND_MESSAGE = "Could not find user by ";
    private final String username;

    public UserNotFoundException(String username) {
        super(USER_NOT_FOUND_MESSAGE + username);
        this.username = username;
    }

    @Override
    public String getMessage() {
        return USER_NOT_FOUND_MESSAGE + username;
    }
}
