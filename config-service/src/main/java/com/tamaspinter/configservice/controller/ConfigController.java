package com.tamaspinter.configservice.controller;

import com.tamaspinter.configservice.exception.ConfigAlreadyExistsException;
import com.tamaspinter.configservice.exception.ConfigNotFoundException;
import com.tamaspinter.configservice.exception.StorageProviderNotFoundException;
import com.tamaspinter.configservice.model.Config;
import com.tamaspinter.configservice.model.User;
import com.tamaspinter.configservice.service.ConfigService;
import com.tamaspinter.configservice.service.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * This controller manages the different storage configurations of users
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/config")
public class ConfigController {

    private final ConfigService configService;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    ResponseEntity<Config> createConfig(@RequestBody Config config, @RequestHeader HttpHeaders headers) {
        try {
            User user = jwtUtil.getUserFromHeader(headers);
            config.setUserId(user.getId());
            Config newConfig = configService.createConfiguration(config);
            return new ResponseEntity<>(newConfig, HttpStatus.OK);
        } catch (ConfigAlreadyExistsException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/list")
    ResponseEntity<List<Config>> getConfigsByUser(@RequestHeader HttpHeaders headers) {
        try {
            User user = jwtUtil.getUserFromHeader(headers);
            List<Config> configs = configService.getUserConfigs(user.getId());
            return new ResponseEntity<>(configs, HttpStatus.OK);
        } catch (ConfigNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/{providerName}")
    ResponseEntity<Config> editConfigAtProvider(@PathVariable String providerName, @RequestBody String credentials, @RequestHeader HttpHeaders headers) {
        try {
            User user = jwtUtil.getUserFromHeader(headers);
            Config config = configService.getUserConfigAtProvider(user.getId(), providerName);
            configService.editConfigCredentials(user.getId(), providerName, credentials);
            return new ResponseEntity<>(config, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{providerName}")
    ResponseEntity<Config> getConfigByProviderName(@PathVariable String providerName, @RequestHeader HttpHeaders headers) {
        try {
            User user = jwtUtil.getUserFromHeader(headers);
            Config config = configService.getUserConfigAtProvider(user.getId(), providerName);
            return new ResponseEntity<>(config, HttpStatus.OK);
        } catch (ConfigNotFoundException | StorageProviderNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
