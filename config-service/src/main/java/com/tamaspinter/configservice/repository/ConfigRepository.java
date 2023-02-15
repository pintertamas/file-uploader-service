package com.tamaspinter.configservice.repository;

import com.tamaspinter.configservice.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {
    Config findConfigByProviderIdAndUserId(Long providerId, Long userId);
    List<Config> findConfigsByUserId(Long userId);
    void deleteAllByProviderId(Long providerId);
}
