package com.tamaspinter.sharingservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "file-upload-service")
public interface FileUploadProxy {
    @GetMapping("/upload/{fileId}")
    ResponseEntity<Boolean> isOwner(@PathVariable Long fileId, @RequestHeader HttpHeaders headers);
}