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

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DriverController.class)
public class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriverService driverService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createDriver_ReturnsCreatedDriver() throws Exception {
        DriverRequest request = new DriverRequest(
                "Иван",                    // name
                "ABC123",                 // carNumber
                "+996 555 123 456",       // phoneNumber
                "Toyota"                  // carModel
        );

        DriverResponse response = new DriverResponse(
                1L, "Иван", "+996 555 123 456", "Toyota", "ABC123", Status.AVAILABLE, LocalDateTime.now(), 0, 0
        );

        Mockito.when(driverService.registerDriver(any(DriverRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Иван"));
    }

    @Test
    public void findById_ReturnsDriver() throws Exception {
        DriverResponse response = new DriverResponse(
                1L, "Иван", "+996 555 123 456", "Toyota", "ABC123", Status.AVAILABLE, LocalDateTime.now(), 0, 0
        );
        Mockito.when(driverService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/driver/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Иван"));
    }

    @Test
    public void findAllByStatus_ReturnsDriversList() throws Exception {
        DriverResponse response1 = new DriverResponse(
                1L, "Иван", "+996 555 123 456", "Toyota", "ABC123", Status.AVAILABLE, LocalDateTime.now(), 0, 0
        );
        DriverResponse response2 = new DriverResponse(
                2L, "Петр", "+996 777 456 789", "Honda", "XYZ789", Status.AVAILABLE, LocalDateTime.now(), 0, 0
        );

        Mockito.when(driverService.findAllByStatus(Status.AVAILABLE)).thenReturn(List.of(response1, response2));

        mockMvc.perform(get("/api/v1/driver/all/by/AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    public void updateStatus_ReturnsUpdatedDriver() throws Exception {
        DriverResponse response = new DriverResponse(
                1L, "Иван", "+996 555 123 456", "Toyota", "ABC123", Status.BUSY, LocalDateTime.now(), 0, 0
        );
        Mockito.when(driverService.updateStatus(eq(1), eq(Status.BUSY))).thenReturn(response);

        mockMvc.perform(put("/api/v1/driver/1")
                        .param("status", "AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }
}
