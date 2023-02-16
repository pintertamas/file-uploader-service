package com.tamaspinter.fileretrievalservice.controller;

import com.tamaspinter.fileretrievalservice.exception.FileNotFoundException;
import com.tamaspinter.fileretrievalservice.model.File;
import com.tamaspinter.fileretrievalservice.service.FileRetrievalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileRetrievalController {

    private final FileRetrievalService fileRetrievalService;

    @GetMapping
    ResponseEntity<List<File>> getAccessibleFiles(@RequestHeader HttpHeaders headers) {
        try {
            List<File> accessibleFiles = fileRetrievalService.getListOfAccessibleFiles(headers);
            //List<File> filteredFiles = fileRetrievalService.filterFiles(accessibleFiles, storage, createdAfter, createdBefore, fileName);
            //List<File> paginatedFiles = fileRetrievalService.paginateFiles(filteredFiles);
            return new ResponseEntity<>(accessibleFiles, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
