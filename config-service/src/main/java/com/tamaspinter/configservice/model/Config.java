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
@Entity(name = "config")
@Table(name = "config")
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    Long id;

    @Column(nullable = false)
    Long userId;

    @Column(nullable = false)
    Long providerId;

    @Column(nullable = false)
    /* this String contains a parsable JSON object
    e.g:    { "username" : "SantaClaus2", "password": "rudolf123" }
    or      { "token": "abcdefgh12345678" } */
    String credentials;

    @Override
    public String toString() {
        return "Config{" +
                "userId=" + userId +
                ", providerId=" + providerId +
                ", credentials='" + credentials + '\'' +
                '}';
    }
}
