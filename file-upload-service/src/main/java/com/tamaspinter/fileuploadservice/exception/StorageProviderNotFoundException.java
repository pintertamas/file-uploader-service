package com.tamaspinter.fileuploadservice.exception;

public class StorageProviderNotFoundException extends Exception {
    private static final String PROVIDER_NOT_FOUND_MESSAGE = "Could not find storage provider";

    public StorageProviderNotFoundException() {
        super(PROVIDER_NOT_FOUND_MESSAGE);
    }
}
