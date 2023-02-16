package com.tamaspinter.sharingservice.exception;

public class AccessNotFoundException extends Exception {
    private static final String ACCESS_NOT_FOUND_MESSAGE = "Could not find access";

    public AccessNotFoundException() {
        super(ACCESS_NOT_FOUND_MESSAGE);
    }
}
