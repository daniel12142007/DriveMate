package com.driver.driver.api;

import com.driver.driver.dto.request.DriverRequest;
import com.driver.driver.dto.response.DriverResponse;
import com.driver.driver.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/driver")
public class DriverController {
    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<DriverResponse> createDriver(@Valid
                                                       @RequestBody DriverRequest request) {
        DriverResponse response = driverService.registerDriver(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}