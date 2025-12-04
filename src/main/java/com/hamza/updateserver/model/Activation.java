package com.hamza.updateserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "activations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, length = 50)
    private String activationKey;

    @Column(unique = true, nullable = false, length = 100)
    private String machineId;

    @Column(unique = true, nullable = false, length = 100)
    private String macAddress;

    @Column
    private Boolean isUsed = false;

    @Column
    private Boolean isTrial = false;

    @Column
    private LocalDateTime activationDate;

    @Column
    private LocalDateTime expirationDate;

    @Column(length = 100)
    private String customerEmail;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDate = LocalDateTime.now();
    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();
}