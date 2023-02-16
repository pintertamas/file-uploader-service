package com.tamaspinter.fileretrievalservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EnableAutoConfiguration
public class File {
    private Long id;
    private String filename;
    private long fileSize;
    private String fileExtension;
    private Long ownerId;
    private Timestamp creationTime;
}
