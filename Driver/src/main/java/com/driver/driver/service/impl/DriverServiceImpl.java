package com.driver.driver.service.impl;

import com.driver.driver.dto.request.DriverRequest;
import com.driver.driver.dto.response.DriverResponse;
import com.driver.driver.model.Driver;
import com.driver.driver.model.enums.Status;
import com.driver.driver.repository.DriverRepository;
import com.driver.driver.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;

    @Override
    public DriverResponse registerDriver(DriverRequest request) {
        if (driverRepository.existsByPhoneNumber(request.phoneNumber()))
            throw new RuntimeException("Phone number must be unique");
        if (driverRepository.existsByCarNumber(request.carNumber()))
            throw new RuntimeException("Car number must be unique");

        Driver driver = Driver.builder()
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .carModel(request.carModel())
                .carNumber(request.carNumber())
                .status(Status.AVAILABLE)
                .rating(0)
                .dateRegistered(LocalDateTime.now())
                .build();
        driverRepository.save(driver);

        return driverRepository.findByIdResponse(driver.getId());
    }
}