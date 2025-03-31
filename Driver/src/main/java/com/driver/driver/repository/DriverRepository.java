package com.driver.driver.repository;

import com.driver.driver.dto.response.DriverResponse;
import com.driver.driver.model.Driver;
import com.driver.driver.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByCarNumber(String carNumber);

    @Query("""
            select new com.driver.driver.dto.response.DriverResponse(
            d.id,
            d.name,
            d.carNumber,
            d.phoneNumber,
            d.carModel,
            d.status,
            d.dateRegistered,
            d.rating
            )
            from Driver d
            where d.id = :id
            """)
    DriverResponse findByIdResponse(@Param(value = "id") Long id);

    @Query("""
            select new com.driver.driver.dto.response.DriverResponse(
            d.id,
            d.name,
            d.carNumber,
            d.phoneNumber,
            d.carModel,
            d.status,
            d.dateRegistered,
            d.rating
            )
            from Driver d
            where d.status = :status
            order by d.lastOrder
            """)
    List<DriverResponse> findAllByStatus(@Param(value = "status") Status status);
}