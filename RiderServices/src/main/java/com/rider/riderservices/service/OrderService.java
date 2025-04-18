package com.rider.riderservices.service;

import com.rider.riderservices.dto.request.OrderRequest;
import com.rider.riderservices.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);

    boolean completedOrder(long driverId);
}