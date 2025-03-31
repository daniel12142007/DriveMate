package com.driver.driver.service;

import com.driver.driver.dto.request.DriverRequest;
import com.driver.driver.dto.response.DriverResponse;
import com.driver.driver.model.enums.Status;

import java.util.List;

public interface DriverService {
    DriverResponse registerDriver(DriverRequest request);

    DriverResponse findById(long driverId);

    List<DriverResponse> findAllByStatus(Status status);
}