package com.driver.driver.service;

import com.driver.driver.dto.request.DriverRequest;
import com.driver.driver.dto.response.DriverResponse;

public interface DriverService {
    DriverResponse registerDriver(DriverRequest request);
}