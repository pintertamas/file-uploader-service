package com.tamaspinter.fileretrievalservice.controller;

import com.tamaspinter.fileretrievalservice.exception.FileNotFoundException;
import com.tamaspinter.fileretrievalservice.model.File;
import com.tamaspinter.fileretrievalservice.service.FileRetrievalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileRetrievalController {

    private final FileRetrievalService fileRetrievalService;

    @GetMapping
    ResponseEntity<PageImpl<File>> getAccessibleFiles(
            @RequestHeader HttpHeaders headers,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long storage,
            @RequestParam(required = false) Timestamp createdAfter,
            @RequestParam(required = false) Timestamp createdBefore,
            @RequestParam(required = false) String fileName)
    {
        try {
            List<File> accessibleFiles = fileRetrievalService.getListOfAccessibleFiles(headers);
            List<File> filteredFiles = fileRetrievalService.filterFiles(accessibleFiles, storage, createdAfter, createdBefore, fileName);
            PageImpl<File> paginatedFiles = fileRetrievalService.paginateFiles(filteredFiles, pageNumber, pageSize);
            return new ResponseEntity<>(paginatedFiles, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
