package com.tamaspinter.configservice.controller;

import com.tamaspinter.configservice.exception.AuthenticationMethodUnchangedException;
import com.tamaspinter.configservice.exception.ProviderAlreadyExistsException;
import com.tamaspinter.configservice.model.AuthType;
import com.tamaspinter.configservice.model.StorageProvider;
import com.tamaspinter.configservice.service.ConfigService;
import com.tamaspinter.configservice.service.StorageProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * This controller handles the registration of new storage providers
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/storage-provider")
public class StorageProviderController {

    private final StorageProviderService storageProviderService;
    private final ConfigService configService;

    @PostMapping("/register-new-provider")
    ResponseEntity<StorageProvider> registerNewProvider(@RequestBody StorageProvider newStorageProvider) {
        try {
            StorageProvider storageProvider = storageProviderService.registerProvider(newStorageProvider);
            return new ResponseEntity<>(storageProvider, HttpStatus.OK);
        } catch (ProviderAlreadyExistsException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/edit-provider")
    ResponseEntity<String> editProviderAuthType(@RequestParam String name, @RequestParam AuthType newAuthType) {
        try {
            storageProviderService.editStorageProviderAuthType(name, newAuthType);
            // remove every config that was set for the storage provider, but with a different authentication type
            configService.deleteEveryConfigForProvider(name);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (AuthenticationMethodUnchangedException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/list")
    ResponseEntity<List<StorageProvider>> listOfStorageProviders() {
        try {
            List<StorageProvider> providers = storageProviderService.listOfProviders();
            if (providers.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return new ResponseEntity<>(providers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    ResponseEntity<StorageProvider> getStorageProviderByName(@RequestParam String name) {
        try {
            StorageProvider storageProvider = storageProviderService.findStorageProviderByName(name);
            if (storageProvider == null) {
                return ResponseEntity.notFound().build();
            }
            return new ResponseEntity<>(storageProvider, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
