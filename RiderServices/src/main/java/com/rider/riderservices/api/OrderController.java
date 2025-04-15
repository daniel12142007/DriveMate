package com.rider.riderservices.api;

import com.rider.riderservices.dto.request.OrderRequest;
import com.rider.riderservices.dto.response.OrderResponse;
import com.rider.riderservices.model.enums.StatusOrder;
import com.rider.riderservices.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> addOrder(@RequestBody OrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.ok(response);
    }
}
