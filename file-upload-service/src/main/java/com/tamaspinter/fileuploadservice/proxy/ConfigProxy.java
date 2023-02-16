package com.tamaspinter.fileuploadservice.proxy;

import com.tamaspinter.fileuploadservice.model.Config;
import com.tamaspinter.fileuploadservice.model.StorageProvider;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "config-service")
public interface ConfigProxy {
    @GetMapping("/storage-provider")
    ResponseEntity<StorageProvider> getStorageProviderByName(@RequestParam String name);

    @GetMapping("/config/{providerName}")
    ResponseEntity<Config> getConfigByProviderName(@PathVariable String providerName, @RequestHeader HttpHeaders headers);
}