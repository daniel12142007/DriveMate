package com.rider.riderservices.dto.response;

import com.rider.riderservices.model.enums.StatusOrder;

public record OrderResponse(
        Long id,
        String address,
        long price,
        Long driverId,
        StatusOrder statusOrder,
        double distanceKm
) {
}