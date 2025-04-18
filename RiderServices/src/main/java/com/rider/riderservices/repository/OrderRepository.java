package com.rider.riderservices.repository;

import com.rider.riderservices.dto.response.OrderResponse;
import com.rider.riderservices.model.Order;
import com.rider.riderservices.model.enums.StatusOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
            select new com.rider.riderservices.dto.response.OrderResponse(
            o.id,
            o.address,
            o.price,
            o.driverId,
            o.statusOrder,
            o.distanceKm
            ) from Order o
            where o.id = :id
            """)
    OrderResponse findByIdResponse(@Param("id") Long id);

    List<Order> findByDriverIdAndStatusOrder(@Param("driverId") Long driverId,
                                             @Param("statusOrder") StatusOrder statusOrder);
}