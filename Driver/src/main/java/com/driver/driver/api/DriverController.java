package com.driver.driver.api;

import com.driver.driver.dto.request.DriverRequest;
import com.driver.driver.dto.response.DriverResponse;
import com.driver.driver.model.enums.Status;
import com.driver.driver.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("{id}")
    public ResponseEntity<DriverResponse> findById(@PathVariable("id") Long id) {
        DriverResponse response = driverService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("all/by/{status}")
    public ResponseEntity<List<DriverResponse>> findAllByStatus(@PathVariable("status") Status status) {
        return ResponseEntity.ok(driverService.findAllByStatus(status));
    }

    @PutMapping("{id}")
    public ResponseEntity<DriverResponse> updateStatus(@PathVariable Long id, @RequestParam Status status) {
        DriverResponse response = driverService.updateStatus(id, status);
        return ResponseEntity.ok(response);
    }
}