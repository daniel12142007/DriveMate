package com.driver.driver.api;

import com.driver.driver.dto.request.DriverRequest;
import com.driver.driver.dto.response.DriverResponse;
import com.driver.driver.model.enums.Status;
import com.driver.driver.service.DriverService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Driver"));
        /*
        status().isCreated(): проверяем, что статус ответа — 201 (Created).

        content().contentType(MediaType.APPLICATION_JSON): проверяем, что тип контента в ответе — JSON.

        jsonPath("$.id").value(1): проверяем, что в ответе поле id равно 1.

        jsonPath("$.name").value("Иван"): проверяем, что в ответе поле name равно "Иван".
         */
    }

}
