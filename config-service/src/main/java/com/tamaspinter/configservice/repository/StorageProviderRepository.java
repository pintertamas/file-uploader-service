package com.tamaspinter.configservice.repository;

import com.tamaspinter.configservice.model.StorageProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageProviderRepository extends JpaRepository<StorageProvider, Long> {
    StorageProvider findStorageProviderByName(String name);
    StorageProvider findStorageProviderById(Long id);
}
