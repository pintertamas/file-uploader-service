package com.tamaspinter.configservice.exception;

import com.tamaspinter.configservice.model.Config;

public class ConfigAlreadyExistsException extends Exception {
    private final transient Config config;

    public ConfigAlreadyExistsException(Config config) {
        super(config.toString() + " config is already registered");
        this.config = config;
    }

    public String getName() {
        return config.toString();
    }
}
