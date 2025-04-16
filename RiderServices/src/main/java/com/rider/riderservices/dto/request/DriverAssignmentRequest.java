package com.rider.riderservices.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class DriverAssignmentRequest implements Serializable {
    private Long orderId;

    public DriverAssignmentRequest() {
    }

    public DriverAssignmentRequest(Long orderId) {
        this.orderId = orderId;
    }
}