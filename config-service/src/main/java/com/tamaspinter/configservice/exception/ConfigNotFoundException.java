package com.tamaspinter.configservice.exception;

public class ConfigNotFoundException extends Exception {
    private static final String CONFIG_NOT_FOUND_MESSAGE = "Could not find config by ";
    private final String config;

    public ConfigNotFoundException(String config) {
        super(CONFIG_NOT_FOUND_MESSAGE + config);
        this.config = config;
    }

    @Override
    public String getMessage() {
        return CONFIG_NOT_FOUND_MESSAGE + config;
    }
}
