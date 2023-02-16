package com.tamaspinter.fileretrievalservice.proxy;

import com.tamaspinter.fileretrievalservice.model.File;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "file-upload-service")
public interface FileUploadProxy {
    @PostMapping("/upload/files-by-ids")
    ResponseEntity<List<File>> getFilesByIds(@RequestBody List<Long> fileIds);
}