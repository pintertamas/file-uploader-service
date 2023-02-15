package com.tamaspinter.configservice.exception;

import com.tamaspinter.configservice.model.StorageProvider;

public class ProviderAlreadyExistsException extends Exception {
    private final transient StorageProvider storageProvider;

    public ProviderAlreadyExistsException(StorageProvider storageProvider) {
        super(storageProvider.getName() + " provider is already registered");
        this.storageProvider = storageProvider;
    }

    public String getName() {
        return storageProvider.getName();
    }
}
