package com.tamaspinter.userservice.exceptions;

public class WeakPasswordException extends Exception {
    public WeakPasswordException() {
        super("Weak password");
    }
}
