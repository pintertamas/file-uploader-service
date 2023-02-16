package com.tamaspinter.sharingservice.exception;

import com.tamaspinter.sharingservice.model.Access;

public class AccessAlreadyExistsException extends Exception {

    public AccessAlreadyExistsException(Access access) {
        super("user (" + access.getUserId() + ") can already access file (" + access.getFileId() + ")");
    }
}