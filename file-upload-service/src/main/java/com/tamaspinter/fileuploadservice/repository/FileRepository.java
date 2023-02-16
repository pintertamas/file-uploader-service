package com.tamaspinter.fileuploadservice.repository;

import com.tamaspinter.fileuploadservice.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    File findFileById(Long id);
    List<File> findFilesByFilenameContaining(String filenamePart);
    List<File> findFilesByFileSizeLessThanEqual(long fileSize);
    List<File> findFilesByFileSizeGreaterThanEqual(long fileSize);
    List<File> findFilesByCreationTimeBefore(Timestamp beforeThis);
    List<File> findFilesByCreationTimeAfter(Timestamp afterThis);
}
