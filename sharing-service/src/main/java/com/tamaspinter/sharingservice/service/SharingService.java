package com.tamaspinter.sharingservice.service;

import com.tamaspinter.sharingservice.exception.AccessAlreadyExistsException;
import com.tamaspinter.sharingservice.exception.AccessNotFoundException;
import com.tamaspinter.sharingservice.model.Access;
import com.tamaspinter.sharingservice.proxy.FileUploadProxy;
import com.tamaspinter.sharingservice.repository.SharingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SharingService {

    private final SharingRepository sharingRepository;
    private final FileUploadProxy fileUploadProxy;

    public boolean assureOwnership(Long fileId, HttpHeaders headers) {
        ResponseEntity<Boolean> isOwnerResponse = fileUploadProxy.isOwner(fileId, headers);
        if (!isOwnerResponse.getStatusCode().equals(HttpStatus.OK) || isOwnerResponse.getBody() == null) {
            return false;
        } else {
            return isOwnerResponse.getBody();
        }
    }

    public Access shareFile(Long userId, Long fileId) throws AccessAlreadyExistsException {
        Access newAccess = sharingRepository.findAccessByUserIdAndFileId(userId, fileId);
        if (newAccess != null) throw new AccessAlreadyExistsException(newAccess);
        else newAccess = new Access();
        newAccess.setUserId(userId);
        newAccess.setFileId(fileId);
        return sharingRepository.save(newAccess);
    }

    public List<Access> listAccessesByFileId(Long fileId) throws AccessNotFoundException {
        List<Access> accesses = sharingRepository.findAccessesByFileId(fileId);
        if (accesses.isEmpty()) {
            throw new AccessNotFoundException();
        }
        else {
            return accesses;
        }
    }

    public List<Access> listOfAccessesByUserId(Long userId) throws AccessNotFoundException {
        List<Access> accesses = sharingRepository.findAccessesByUserId(userId);
        if (accesses.isEmpty()) {
            throw new AccessNotFoundException();
        }
        else {
            return accesses;
        }
    }

    public void revokeAccessFromFile(Long userId, Long fileId) {
        sharingRepository.deleteAccessByUserIdAndFileId(userId, fileId);
    }

}
