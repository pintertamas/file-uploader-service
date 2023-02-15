package com.tamaspinter.configservice.repository;

import com.tamaspinter.configservice.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {
    Config findConfigByProviderIdAndUserId(Long providerId, Long userId);
    Config findConfigByUserId(Long userId);
}
