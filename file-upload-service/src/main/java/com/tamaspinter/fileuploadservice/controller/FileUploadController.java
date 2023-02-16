package com.tamaspinter.fileuploadservice.controller;

import com.tamaspinter.fileuploadservice.exception.UserNotFoundException;
import com.tamaspinter.fileuploadservice.model.File;
import com.tamaspinter.fileuploadservice.service.FileUploadService;
import com.tamaspinter.fileuploadservice.service.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/upload")
public class FileUploadController {

    private final FileUploadService fileUploadService;
    private final JwtUtil jwtUtil;

    @PostMapping()
    ResponseEntity<File> uploadFile(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("providers") List<String> providerNames,
            @RequestHeader HttpHeaders headers
    ) {
        try {
            log.info("List of providers: " + providerNames.toString());
            Long userId = jwtUtil.getUserIdFromHeader(headers);
            File file = fileUploadService.uploadFile(userId, multipartFile, providerNames, headers);
            return new ResponseEntity<>(file, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{fileId}")
    ResponseEntity<Boolean> isOwner(@PathVariable Long fileId, @RequestHeader HttpHeaders headers) {
        try {
            Long userId = jwtUtil.getUserIdFromHeader(headers);
            boolean isOwner = fileUploadService.isOwner(userId, fileId);
            return new ResponseEntity<>(isOwner, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
