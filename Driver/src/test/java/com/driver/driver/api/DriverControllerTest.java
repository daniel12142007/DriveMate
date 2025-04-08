package com.driver.driver.api;

import com.driver.driver.dto.request.DriverRequest;
import com.driver.driver.dto.response.DriverResponse;
import com.driver.driver.model.enums.Status;
import com.driver.driver.service.DriverService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DriverController.class)
public class DriverControllerTest {
    @Autowired
    private MockMvc mockMvc; // Используется для отправки HTTP запросов и проверки ответов в тесте
    @MockBean
    private DriverService driverService; // Вместо реального сервиса будет использоваться заглушка, позволяющая контролировать поведение в тестах.
    /*
    Когда мы используем моки (заглушки), данные не сохраняются в базе данных.
     Мок — это просто временная замена для реальных объектов (например, сервисов или репозиториев).
     Вместо того чтобы взаимодействовать с настоящей базой данных, мок будет "подменять" её поведение.
    */
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Created driver")
    @Test
    public void createDriver_ReturnsCreatedDriver() throws Exception {
        DriverRequest request = new DriverRequest(
                "Driver",
                "AA32",
                "+996 700 700 700",
                "Toyota"
        );

        DriverResponse response = new DriverResponse(
                1L,
                "Driver",
                "AA32",
                "+996 700 700 700",
                "Toyota",
                Status.AVAILABLE
        );

        Mockito.when(
                driverService.registerDriver(
                        request
                )).thenReturn(response);
//        when - Он перехватывает вызов метода у driverService и ожидает, что мы скажем: «а что возвращать?»

        mockMvc.perform(
                        post("/api/v1/driver")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Driver"));
        /*
        status().isCreated(): проверяем, что статус ответа — 201 (Created).

        content().contentType(MediaType.APPLICATION_JSON): проверяем, что тип контента в ответе — JSON.

        jsonPath("$.id").value(1): проверяем, что в ответе поле id равно 1.

        jsonPath("$.name").value("Иван"): проверяем, что в ответе поле name равно "Иван".
         */
    }

    @DisplayName("Find by id driver")
    @Test
    public void findById_ReturnsDriver() throws Exception {
        DriverResponse response = new DriverResponse(
                1L,
                "Driver",
                "AA32",
                "+996 700 700 700",
                "Toyota",
                Status.AVAILABLE
        );

        Mockito.when(
                driverService.findById(1)
        ).thenReturn(response);

        mockMvc.perform(get("/api/v1/driver/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Driver"));
    }

    @DisplayName("Find all driver")
    @Test
    public void findAll_ReturnsAllDrivers() throws Exception {
        DriverResponse response1 = new DriverResponse(
                1L,
                "Driver",
                "AA32",
                "+996 700 700 700",
                "Toyota",
                Status.AVAILABLE
        );
        DriverResponse response2 = new DriverResponse(
                2L,
                "Driver",
                "AA32",
                "+996 700 700 700",
                "Toyota",
                Status.AVAILABLE
        );

        Mockito.when(
                driverService.findAllByStatus(Status.AVAILABLE)
        ).thenReturn(List.of(response1, response2));

        mockMvc.perform(get("/api/v1/driver/all/by/{status}", Status.AVAILABLE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @DisplayName("Update status driver")
    @Test
    public void updateStatus_ReturnsUpdatedStatus() throws Exception {
        long driverId = 1L;
        Status newStatus = Status.BUSY;

        DriverResponse response = new DriverResponse(
                1L,
                "Driver",
                "AA32",
                "+996 700 700 700",
                "Toyota",
                Status.BUSY
        );

        Mockito.when(
                driverService.updateStatus(driverId, newStatus)
        ).thenReturn(response);

        mockMvc.perform(
                        put("/api/v1/driver/{id}", driverId)
                                .param("status", newStatus.name())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value(newStatus.name()));
    }


}