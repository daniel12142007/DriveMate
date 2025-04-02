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
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

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
                        "http://localhost:8080/api/v1/driver/all/by/" + Status.AVAILABLE,
                        DriverResponse[].class);
        if (responses.length < 1)
            throw new RuntimeException("Not found available drivers");
        order.setDriverId(responses[0].id());
        orderRepository.save(order);
        return orderRepository.findByIdResponse(order.getId());
    }

}