package com.rider.riderservices.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class DriverAssignedResponse implements Serializable {
    private Long orderId;
    private Long driverId;

    public DriverAssignedResponse() {
    }

    public DriverAssignedResponse(Long orderId, Long driverId) {
        this.orderId = orderId;
        this.driverId = driverId;
    }
}