package com.driver.driver.service.impl;

import com.driver.driver.dto.request.DriverAssignmentRequest;
import com.driver.driver.dto.request.DriverRequest;
import com.driver.driver.dto.response.DriverAssignedResponse;
import com.driver.driver.dto.response.DriverResponse;
import com.driver.driver.model.Driver;
import com.driver.driver.model.enums.Status;
import com.driver.driver.repository.DriverRepository;
import com.driver.driver.service.DriverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;

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
                .lastOrder(LocalDateTime.now())
                .build();
        driverRepository.save(driver);

        return driverRepository.findByIdResponse(driver.getId());
    }

    @Override
    public DriverResponse findById(long driverId) {
        DriverResponse response = driverRepository.findByIdResponse(driverId);
        if (response == null)
            throw new RuntimeException("Driver not found");
        return response;
    }

    @Override
    public List<DriverResponse> findAllByStatus(Status status) {
        return driverRepository.findAllByStatus(status);
    }

    @Override
    public DriverResponse updateStatus(long driverId, Status status) {
        Driver driver =
                driverRepository.findById(driverId).orElseThrow(() -> new RuntimeException("Driver not found"));
        driver.setStatus(status);
        driverRepository.save(driver);
        return driverRepository.findByIdResponse(driverId);
    }

    @Override
    public boolean completedOrder(long driverId) {
        Driver driver = driverRepository.findById(driverId).orElseThrow(
                () -> new RuntimeException("Driver not found"));
        ResponseEntity<Boolean> successRequest = restTemplate.exchange(
                "http://Rider/api/v1/order/complete/" + driverId,
                HttpMethod.PUT,
                null,
                Boolean.class);
        driver.setStatus(Status.AVAILABLE);

        if (Boolean.FALSE.equals(successRequest.getBody()))
            throw new RuntimeException("Не удалось завершить заказ, у вас нету заказов");
        driverRepository.save(driver);

        return true;
    }

    @RabbitListener(queues = "DriverRequest")
    public void assignDriverToOrder(DriverAssignmentRequest request) {
        Long orderId = request.getOrderId();

        List<DriverResponse> drivers = driverRepository.findAllByStatus(Status.AVAILABLE);
        if (drivers.isEmpty()) {
            log.warn("Нет доступных водителей для заказа {}", orderId);
            return;
        }
        DriverResponse response = drivers.get(0);

        updateStatus(response.id(), Status.BUSY);

        rabbitTemplate.convertAndSend("DriverAssigned",
                new DriverAssignedResponse(orderId, response.id()));
    }
}