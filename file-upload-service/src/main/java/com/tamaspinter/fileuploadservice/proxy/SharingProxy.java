package com.tamaspinter.fileuploadservice.proxy;

import com.tamaspinter.fileuploadservice.model.Access;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "sharing-service")
@Retryable(maxAttemptsExpression = "3", backoff = @Backoff(delayExpression = "500", multiplierExpression = "2"))
public interface SharingProxy {
    @PostMapping("/share")
    ResponseEntity<Access> shareFile(@RequestParam Long newUserId, @RequestParam Long fileId, @RequestHeader HttpHeaders headers);
}