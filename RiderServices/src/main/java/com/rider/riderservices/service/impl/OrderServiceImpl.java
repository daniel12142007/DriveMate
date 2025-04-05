package com.rider.riderservices.service.impl;

import com.rider.riderservices.dto.request.OrderRequest;
import com.rider.riderservices.dto.response.DriverResponse;
import com.rider.riderservices.dto.response.OrderResponse;
import com.rider.riderservices.model.Order;
import com.rider.riderservices.model.enums.Status;
import com.rider.riderservices.model.enums.StatusOrder;
import com.rider.riderservices.repository.OrderRepository;
import com.rider.riderservices.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final String driverUrl = "http://localhost:8080/api/v1/driver";

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        Order order = Order.builder()
                .address(request.address())
                .dateOrder(LocalDateTime.now())
                .statusOrder(StatusOrder.PROCESS)
                .distanceKm(request.distanceKm())
                .price(Math.round(request.distanceKm()) * 50)
                .build();

        log.info("Поиск свободного водителя");
        DriverResponse[] responses =
                restTemplate.getForObject(
                        driverUrl + "/all/by/" + Status.AVAILABLE,
                        DriverResponse[].class);
        if (responses == null || responses.length < 1)
            throw new RuntimeException("Not found available drivers");
        Long driverId = responses[0].id();

        if (driverId == null)
            throw new RuntimeException("Not found available driver");

        order.setDriverId(driverId);

        DriverResponse response = restTemplate.exchange(
                driverUrl + "/" + driverId + "?status=" + Status.BUSY,
                HttpMethod.PUT,
                null,
                DriverResponse.class
        ).getBody();

        if (response == null)
            throw new RuntimeException("Not success");
        log.info("Поменяться статус водителя на BUSY с id {}", response.id());

        orderRepository.save(order);

        return orderRepository.findByIdResponse(order.getId());
    }
}