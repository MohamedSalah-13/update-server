package com.hamza.updateserver.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Entity
@Table(name = "updates")
public class UpdateInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String version;

    @Column(nullable = false)
    private LocalDateTime releaseDate;

    @Column(nullable = false)
    private String downloadUrl;

    @ElementCollection
    @MapKeyColumn(name = "language")
    @Column(name = "changelog_text")
    @CollectionTable(name = "update_changelog", joinColumns = @JoinColumn(name = "update_id"))
    private Map<String, String> changelog;

    @Column(nullable = false)
    private boolean required;

    private String minSupportedVersion;

    @Column(nullable = false)
    private boolean active = true;

    private Long fileSize; // بالبايتات

    private String checksum; // SHA-256
}
