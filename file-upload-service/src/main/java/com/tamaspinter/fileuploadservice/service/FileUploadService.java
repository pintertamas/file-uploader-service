package com.tamaspinter.fileuploadservice.service;

import com.tamaspinter.fileuploadservice.exception.StorageProviderNotFoundException;
import com.tamaspinter.fileuploadservice.model.AuthType;
import com.tamaspinter.fileuploadservice.model.Config;
import com.tamaspinter.fileuploadservice.model.File;
import com.tamaspinter.fileuploadservice.model.StorageProvider;
import com.tamaspinter.fileuploadservice.proxy.ConfigProxy;
import com.tamaspinter.fileuploadservice.proxy.SharingProxy;
import com.tamaspinter.fileuploadservice.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileUploadService {

    private static final String CONTENT_TYPE = "Content-Type";

    private final FileRepository fileRepository;
    private final ConfigProxy configProxy;
    private final SharingProxy sharingProxy;

    private File createFile(Long userId, MultipartFile multipartFile) {
        File file = new File();
        file.setOwnerId(userId);
        file.setFileSize(multipartFile.getSize());
        file.setFileExtension(multipartFile.getContentType());
        file.setFilename(multipartFile.getOriginalFilename());
        long nowInMillis = System.currentTimeMillis();
        Timestamp nowInTimestamps = new Timestamp(nowInMillis);
        file.setCreationTime(nowInTimestamps);
        return file;
    }

    private List<StorageProvider> getProvidersFromNames(List<String> providerNames) {
        List<StorageProvider> providers = new ArrayList<>();
        providerNames.forEach(providerName -> {
            ResponseEntity<StorageProvider> providerResponse = configProxy.getStorageProviderByName(providerName);
            if (providerResponse.getStatusCode().equals(HttpStatus.OK)) {
                providers.add(providerResponse.getBody());
            }
        });
        return providers;
    }

    private java.io.File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        String filename = multipartFile.getName();
        java.io.File convertedFile = new java.io.File(filename);
        try (FileOutputStream fileOutputStream = new FileOutputStream(convertedFile)) {
            fileOutputStream.write(multipartFile.getBytes());
        }
        return convertedFile;
    }

    private void populateHeader(HttpHeaders headers, Config config, StorageProvider provider) throws JSONException {
        if (config == null) return;
        // parse JSON data from config
        JSONObject credentialsJSON = new JSONObject(config.getCredentials());
        // based on storage auth type parse the string into a json and put it into the header
        if (provider.getAuthType().equals(AuthType.TOKEN)) {
            String token = credentialsJSON.getString("token");
            headers.add("token", token);
        } else if (provider.getAuthType().equals(AuthType.BASIC_AUTH)) {
            String username = credentialsJSON.getString("username");
            String password = credentialsJSON.getString("password");
            headers.add("username", username);
            headers.add("password", password);
        }
    }

    private Config getUserConfigForSpecificProvider(StorageProvider provider, HttpHeaders headers) {
        headers.remove(CONTENT_TYPE);
        headers.add(CONTENT_TYPE, "application/json");
        ResponseEntity<Config> configResponse = configProxy.getConfigByProviderName(provider.getName(), headers);
        if (!configResponse.getStatusCode().equals(HttpStatus.OK)) {
            return null;
        }
        return configResponse.getBody();
    }

    private void sendRequestToStorageProvider(MultipartFile multipartFile, StorageProvider provider, HttpHeaders headers) throws IOException {
        java.io.File file = convertMultipartFileToFile(multipartFile);
        RestTemplate restTemplate = new RestTemplate();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("file", new FileSystemResource(file));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Void> response = restTemplate.exchange(provider.getUrl(), HttpMethod.POST, requestEntity, Void.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("File uploaded successfully!");
        } else {
            log.error("File upload failed with status code: " + response.getStatusCode());
        }
    }

    public void uploadFileToStorage(
            MultipartFile multipartFile,
            StorageProvider provider,
            HttpHeaders headers
    ) throws JSONException, IOException {
        Config config = getUserConfigForSpecificProvider(provider, headers);
        populateHeader(headers, config, provider);
        sendRequestToStorageProvider(multipartFile, provider, headers);
    }

    public File uploadFile(Long userId, MultipartFile multipartFile, List<String> providerNames, HttpHeaders headers) throws StorageProviderNotFoundException {
        File file = createFile(userId, multipartFile);
        List<StorageProvider> providers = getProvidersFromNames(providerNames);
        if (providers.isEmpty()) {
            throw new StorageProviderNotFoundException();
        }
        providers.forEach(provider -> {
            try {
                uploadFileToStorage(multipartFile, provider, headers);
            } catch (JSONException | IOException e) {
                log.error(e.getMessage());
            }
        });
        File savedFile = fileRepository.save(file);
        headers.remove(CONTENT_TYPE);
        headers.add(CONTENT_TYPE, "application/json");
        sharingProxy.shareFile(userId, savedFile.getId(), headers);
        return savedFile;
    }

    public boolean isOwner(Long userId, Long fileId) {
        File file = fileRepository.findFileById(fileId);
        if (file == null) return false;
        return file.getOwnerId().equals(userId);
    }
}
