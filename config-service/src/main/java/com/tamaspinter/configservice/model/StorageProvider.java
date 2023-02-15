package com.tamaspinter.configservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "storage_provider")
@Table(name = "storage_provider")
public class StorageProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    Long id;

    @Column(unique = true, nullable = false)
    String name;

    @Column(nullable = false)
    AuthType authType;

    @Column(nullable = false)
    String url;
}
