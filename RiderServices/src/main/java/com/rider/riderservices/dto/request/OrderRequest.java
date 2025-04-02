package com.rider.riderservices.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public record OrderRequest(
        @NotBlank(message = "Address cannot be null")
        String address,
        @DecimalMin(value = "0.1", message = "Distance of the bridge should be more than 0.1 km")
        double distanceKm
) {
}