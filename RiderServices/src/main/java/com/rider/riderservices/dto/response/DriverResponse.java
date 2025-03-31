package com.rider.riderservices.dto.response;


import com.rider.riderservices.model.enums.Status;

import java.time.LocalDateTime;

public record DriverResponse(
        Long id,
        String name,
        String carNumber,
        String phoneNumber,
        String carModel,
        Status status,
        LocalDateTime dateRegister,
        float rating,
        long countReviews
) {
    public DriverResponse(Long id, String name, String carNumber,
                          String phoneNumber, String carModel,
                          Status status, LocalDateTime dateRegister) {
        this(id, name, carNumber, phoneNumber, carModel, status, dateRegister, 0, 0);
    }

    public DriverResponse(Long id, String name, String carNumber,
                          String phoneNumber, String carModel,
                          Status status, LocalDateTime dateRegister,
                          float rating) {
        this(id, name, carNumber, phoneNumber, carModel, status, dateRegister, rating, 0);
    }
}