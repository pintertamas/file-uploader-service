package com.tamaspinter.configservice.service;

import com.tamaspinter.configservice.exception.ConfigAlreadyExistsException;
import com.tamaspinter.configservice.exception.StorageProviderNotFoundException;
import com.tamaspinter.configservice.model.Config;
import com.tamaspinter.configservice.model.StorageProvider;
import com.tamaspinter.configservice.repository.ConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConfigService {

    private final ConfigRepository configRepository;
    private final StorageProviderService storageProviderService;

    public Config createConfiguration(Config newConfig) throws ConfigAlreadyExistsException, StorageProviderNotFoundException {
        StorageProvider storageProvider = storageProviderService.findStorageProviderById(newConfig.getProviderId());
        if (storageProvider == null) {
            throw new StorageProviderNotFoundException(newConfig.getProviderId().toString());
        }
        if (configRepository.findConfigByProviderIdAndUserId(newConfig.getProviderId(), newConfig.getUserId()) != null) {
            throw new ConfigAlreadyExistsException(newConfig);
        }
        return configRepository.save(newConfig);
    }

    public void editConfigCredentials(Long userId, String token) {
        Config config = configRepository.findConfigByUserId(userId);
        String credentials = "{\"token\":" + token + "}";
        config.setCredentials(credentials);
        configRepository.save(config);
    }

    public void editConfigCredentials(Long userId, String username, String password) {
        Config config = configRepository.findConfigByUserId(userId);
        String credentials = "{\"username\":" + username + ",\"password\":" + password + "}";
        config.setCredentials(credentials);
        configRepository.save(config);
    }

    public boolean userConfigExistsAtProvider(Long userId, String providerName) {
        StorageProvider storageProvider = storageProviderService.findStorageProviderByName(providerName);
        Config config = configRepository.findConfigByProviderIdAndUserId(storageProvider.getId(), userId);
        return config != null;
    }

}
