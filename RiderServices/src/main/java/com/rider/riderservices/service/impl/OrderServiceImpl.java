package com.rider.riderservices.service.impl;

import com.rider.riderservices.dto.request.DriverAssignmentRequest;
import com.rider.riderservices.dto.request.OrderRequest;
import com.rider.riderservices.dto.response.DriverAssignedResponse;
import com.rider.riderservices.dto.response.OrderResponse;
import com.rider.riderservices.model.Order;
import com.rider.riderservices.model.enums.StatusOrder;
import com.rider.riderservices.repository.OrderRepository;
import com.rider.riderservices.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        Order order = Order.builder()
                .address(request.address())
                .dateOrder(LocalDateTime.now())
                .distanceKm(request.distanceKm())
                .price(Math.round(request.distanceKm()) * 50)
                .build();

        orderRepository.save(order);

        log.info("Отправка запроса в очередь, для поиска свободного водителя");
        DriverAssignmentRequest driverAssignmentRequest = new DriverAssignmentRequest(order.getId());
        rabbitTemplate.convertAndSend("DriverRequest", driverAssignmentRequest);

        return orderRepository.findByIdResponse(order.getId());
    }

    @Override
    public boolean completedOrder(long driverId) {
        List<Order> orderList = orderRepository.findByDriverIdAndStatusOrder(
                driverId,
                StatusOrder.COMPLETE);

        if (orderList.isEmpty()) return false;

        orderList.forEach(order -> {
            order.setStatusOrder(StatusOrder.COMPLETE);
        });

        orderRepository.saveAll(orderList);
        return true;
    }

    @RabbitListener(queues = "DriverAssigned")
    public void handleDriverAssigned(DriverAssignedResponse message) {
        Order order = orderRepository.findById(message.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (message.getDriverId() == null)
            order.setStatusOrder(StatusOrder.CANCELLED);
        else {
            order.setStatusOrder(StatusOrder.PROCESS);
            order.setDriverId(message.getDriverId());
        }
        orderRepository.save(order);

        log.info("Назначен водитель с id {} для заказа {}", message.getDriverId(), message.getOrderId());
    }
}