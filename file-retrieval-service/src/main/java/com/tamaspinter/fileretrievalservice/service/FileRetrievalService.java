package com.tamaspinter.fileretrievalservice.service;

import com.tamaspinter.fileretrievalservice.exception.FileNotFoundException;
import com.tamaspinter.fileretrievalservice.model.File;
import com.tamaspinter.fileretrievalservice.proxy.FileUploadProxy;
import com.tamaspinter.fileretrievalservice.proxy.SharingProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileRetrievalService {

    private final SharingProxy sharingProxy;
    private final FileUploadProxy fileUploadProxy;

    private List<Long> getAccessibleFileIds(HttpHeaders headers) throws FileNotFoundException {
        ResponseEntity<List<Long>> accessibleIdsResponse = sharingProxy.getListOfAccessedFileIds(headers);
        if (!accessibleIdsResponse.getStatusCode().equals(HttpStatus.OK)
                || Objects.requireNonNull(accessibleIdsResponse.getBody()).isEmpty()) {
            throw new FileNotFoundException();
        }
        return accessibleIdsResponse.getBody();
    }

    private List<File> getAccessibleFiles(List<Long> accessibleIds) throws FileNotFoundException {
        ResponseEntity<List<File>> accessibleFilesResponse = fileUploadProxy.getFilesByIds(accessibleIds);
        if (!accessibleFilesResponse.getStatusCode().equals(HttpStatus.OK)
                || Objects.requireNonNull(accessibleFilesResponse.getBody()).isEmpty()) {
            throw new FileNotFoundException();
        }
        return accessibleFilesResponse.getBody();
    }

    public List<File> getListOfAccessibleFiles(HttpHeaders headers) throws FileNotFoundException {
        List<Long> accessibleFileIds = getAccessibleFileIds(headers);
        return getAccessibleFiles(accessibleFileIds);
    }
}
