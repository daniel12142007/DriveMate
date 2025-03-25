package com.driver.driver.model;

import com.driver.driver.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String carNumber;

    @Column(unique = true)
    private String phoneNumber;

    private String carModel;

    private float rating;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime dateRegistered;
}