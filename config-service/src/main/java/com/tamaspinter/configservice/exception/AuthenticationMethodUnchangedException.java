package com.tamaspinter.configservice.exception;

import com.tamaspinter.configservice.model.AuthType;

public class AuthenticationMethodUnchangedException extends Exception {

    public AuthenticationMethodUnchangedException(AuthType authType) {
        super("Authentication type is already set to the requested one: " + authType.toString());
    }
}
