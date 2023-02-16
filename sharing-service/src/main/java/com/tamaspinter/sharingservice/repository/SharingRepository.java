package com.tamaspinter.sharingservice.repository;

import com.tamaspinter.sharingservice.model.Access;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SharingRepository extends JpaRepository<Access, Long> {
    List<Access> findAccessesByFileId(Long fileId);
    List<Access> findAccessesByUserId(Long userId);
    Access findAccessByUserIdAndFileId(Long userId, Long fileId);
    void deleteAccessByUserIdAndFileId(Long userId, Long fileId);
}
