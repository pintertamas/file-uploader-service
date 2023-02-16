package com.tamaspinter.sharingservice.controller;

import com.tamaspinter.sharingservice.exception.AccessAlreadyExistsException;
import com.tamaspinter.sharingservice.exception.AccessNotFoundException;
import com.tamaspinter.sharingservice.exception.UserNotFoundException;
import com.tamaspinter.sharingservice.model.Access;
import com.tamaspinter.sharingservice.service.JwtUtil;
import com.tamaspinter.sharingservice.service.SharingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/share")
public class SharingController {

    private final SharingService sharingService;
    private final JwtUtil jwtUtil;

    @PostMapping()
    ResponseEntity<Access> shareFile(
            @RequestParam Long newUserId,
            @RequestParam Long fileId,
            @RequestHeader HttpHeaders headers
    ) {
        try {
            boolean isOwner = sharingService.assureOwnership(fileId, headers);
            if (!isOwner) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            Access access = sharingService.shareFile(newUserId, fileId);
            return new ResponseEntity<>(access, HttpStatus.OK);
        } catch (AccessAlreadyExistsException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/accessed-files")
    ResponseEntity<List<Long>> getListOfAccessedFileIds(@RequestHeader HttpHeaders headers) {
        try {
            List<Long> accessedFileIds = new ArrayList<>();
            Long userId = jwtUtil.getUserIdFromHeader(headers);
            List<Access> accesses = sharingService.listOfAccessesByUserId(userId);
            accesses.forEach(access -> accessedFileIds.add(access.getFileId()));
            return new ResponseEntity<>(accessedFileIds, HttpStatus.OK);
        } catch (UserNotFoundException | AccessNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
