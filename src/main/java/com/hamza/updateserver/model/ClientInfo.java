package com.hamza.updateserver.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "client_info")
public class ClientInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String clientId; // UUID فريد لكل عميل

    @Column(nullable = false)
    private String currentVersion;

    private String osName;
    private String osVersion;
    private String javaVersion;

    @Column(nullable = false)
    private LocalDateTime lastSeen;

    @Column(nullable = false)
    private LocalDateTime firstSeen;

    private String ipAddress;

    @Column(nullable = false)
    private int checkCount = 0;
}
