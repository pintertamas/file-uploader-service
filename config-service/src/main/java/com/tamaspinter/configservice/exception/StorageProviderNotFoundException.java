package com.tamaspinter.configservice.exception;

public class StorageProviderNotFoundException extends Exception {
    private static final String PROVIDER_NOT_FOUND_MESSAGE = "Could not find storage provider by ";
    private final String provider;

    public StorageProviderNotFoundException(String provider) {
        super(PROVIDER_NOT_FOUND_MESSAGE + provider);
        this.provider = provider;
    }

    @Override
    public String getMessage() {
        return PROVIDER_NOT_FOUND_MESSAGE + provider;
    }
}
