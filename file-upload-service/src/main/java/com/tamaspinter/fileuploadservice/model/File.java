package com.tamaspinter.fileuploadservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EnableAutoConfiguration
@Entity(name = "File")
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private long fileSize;

    @Column
    private String fileExtension;

    @Column(nullable = false)
    private Long ownerId;

    @CreatedDate
    @Column(name = "creation_time")
    private Timestamp creationTime;
}
