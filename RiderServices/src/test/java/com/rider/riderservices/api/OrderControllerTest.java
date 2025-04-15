package com.rider.riderservices.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rider.riderservices.dto.request.OrderRequest;
import com.rider.riderservices.dto.response.OrderResponse;
import com.rider.riderservices.model.enums.StatusOrder;
import com.rider.riderservices.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("order create")
    @Test
    public void orderCreate() throws Exception {
        OrderResponse response
                = new OrderResponse(1L,
                "address",
                500,
                1L,
                StatusOrder.PROCESS,
                5);

        OrderRequest request
                = new OrderRequest("address", 5);

        Mockito.when(
                        orderService.createOrder(request)
                )
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/v1/order")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.price").value(500))
                .andExpect(jsonPath("$.driverId").value(1L))
                .andExpect(jsonPath("$.statusOrder").value(String.valueOf(StatusOrder.PROCESS)))
                .andExpect(jsonPath("$.distanceKm").value(5.0));
    }
}