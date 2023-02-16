package com.tamaspinter.fileuploadservice.proxy;

import com.tamaspinter.fileuploadservice.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserProxy {
    @GetMapping("/user/user-by-username/{username}")
    ResponseEntity<User> findUserByUsername(@PathVariable String username);
}