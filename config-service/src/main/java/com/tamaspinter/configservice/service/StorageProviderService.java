package com.tamaspinter.configservice.service;

import com.tamaspinter.configservice.exception.AuthenticationMethodUnchangedException;
import com.tamaspinter.configservice.exception.ProviderAlreadyExistsException;
import com.tamaspinter.configservice.model.AuthType;
import com.tamaspinter.configservice.model.StorageProvider;
import com.tamaspinter.configservice.repository.StorageProviderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class StorageProviderService {

    final StorageProviderRepository storageProviderRepository;

    public StorageProvider registerProvider(StorageProvider newProvider) throws ProviderAlreadyExistsException {
        if (storageProviderRepository.findStorageProviderByName(newProvider.getName()) != null) {
            throw new ProviderAlreadyExistsException(newProvider);
        }
        return storageProviderRepository.save(newProvider);
    }

    public void editStorageProviderAuthType(String name, AuthType newAuthType) throws AuthenticationMethodUnchangedException {
        StorageProvider storageProvider = storageProviderRepository.findStorageProviderByName(name);
        if (newAuthType.equals(storageProvider.getAuthType())) {
            throw new AuthenticationMethodUnchangedException(newAuthType);
        }
        storageProvider.setAuthType(newAuthType);
        storageProviderRepository.save(storageProvider);
    }

    public List<StorageProvider> listOfProviders() {
        return storageProviderRepository.findAll();
    }

    public StorageProvider findStorageProviderByName(String name) {
        return storageProviderRepository.findStorageProviderByName(name);
    }

    public StorageProvider findStorageProviderById(Long id) {
        return storageProviderRepository.findStorageProviderById(id);
    }

}
