package com.rider.riderservices.dto.request;

import com.rider.riderservices.model.enums.StatusOrder;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record OrderRequest(
        @NotBlank(message = "Address cannot be null")
        String address,
        @NotEmpty
        StatusOrder statusOrder,
        @DecimalMin(value = "0.1", message = "Distance of the bridge should be more than 0.1 km")
        double distanceKm
) {
}