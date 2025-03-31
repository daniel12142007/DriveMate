package com.rider.riderservices.model;

import com.rider.riderservices.model.enums.StatusOrder;
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
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    private long price;

    private Long driverId;

    @Enumerated(EnumType.STRING)
    private StatusOrder statusOrder;

    private double distanceKm;

    private LocalDateTime dateOrder;
}