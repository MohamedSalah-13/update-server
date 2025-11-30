package com.hamza.updateserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(unique = true, nullable = false, length = 50)
    private String activationKey;

    @Column(length = 100)
    private String machineId;

    @Column
    private Boolean isUsed = false;

    @Column
    private LocalDateTime activationDate;

    @Column
    private LocalDateTime expirationDate;

    @Column(length = 100)
    private String customerEmail;
}