package com.tamaspinter.fileretrievalservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "sharing-service")
public interface SharingProxy {
    @GetMapping("/share/accessed-files")
    ResponseEntity<List<Long>> getListOfAccessedFileIds(@RequestHeader HttpHeaders headers);
}