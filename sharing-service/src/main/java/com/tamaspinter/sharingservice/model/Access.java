package com.tamaspinter.sharingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EnableAutoConfiguration
@Entity(name = "Access")
@Table(name = "accesses")
public class Access {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    Long id;

    @Column(nullable = false)
    Long userId;

    @Column(nullable = false)
    Long fileId;
}
