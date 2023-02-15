package com.tamaspinter.configservice.service;

import com.tamaspinter.configservice.exception.ConfigAlreadyExistsException;
import com.tamaspinter.configservice.exception.ConfigNotFoundException;
import com.tamaspinter.configservice.exception.StorageProviderNotFoundException;
import com.tamaspinter.configservice.model.Config;
import com.tamaspinter.configservice.model.StorageProvider;
import com.tamaspinter.configservice.repository.ConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public void editConfigCredentials(Long userId, String providerName, String credentials) throws ConfigNotFoundException, StorageProviderNotFoundException {
        Config config = getUserConfigAtProvider(userId, providerName);
        config.setCredentials(credentials);
        configRepository.save(config);
    }

    @Transactional
    public void deleteEveryConfigForProvider(String providerName) {
        StorageProvider storageProvider = storageProviderService.findStorageProviderByName(providerName);
        configRepository.deleteAllByProviderId(storageProvider.getId());
    }

    public Config getUserConfigAtProvider(Long userId, String providerName) throws StorageProviderNotFoundException, ConfigNotFoundException {
        StorageProvider storageProvider = storageProviderService.findStorageProviderByName(providerName);
        if (storageProvider == null) {
            throw new StorageProviderNotFoundException(providerName);
        }
        Config config = configRepository.findConfigByProviderIdAndUserId(storageProvider.getId(), userId);
        if (config == null) {
            throw new ConfigNotFoundException(providerName);
        }
        return config;
    }

    public List<Config> getUserConfigs(Long userId) throws ConfigNotFoundException {
        List<Config> configs = configRepository.findConfigsByUserId(userId);
        if (configs.isEmpty()) {
            throw new ConfigNotFoundException(userId.toString());
        }
        return configs;
    }

}
