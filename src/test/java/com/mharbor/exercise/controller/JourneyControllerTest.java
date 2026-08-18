package com.mharbor.exercise.controller;

import com.mharbor.exercise.service.JourneyPricingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JourneyController.class)
class JourneyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JourneyPricingService pricingService;

    @Test
    void returnsCalculatedCost() throws Exception {
        when(pricingService.calculateCost(any(), any())).thenReturn(new BigDecimal("47.50"));

        mockMvc.perform(post("/api/journeys/cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"distanceKm\": 200, \"costPerKm\": 0.25}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCost").value(47.50));
    }

    @Test
    void rejectsNegativeDistance() throws Exception {
        mockMvc.perform(post("/api/journeys/cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"distanceKm\": -10, \"costPerKm\": 0.25}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsMissingFields() throws Exception {
        mockMvc.perform(post("/api/journeys/cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejectsNonNumericFieldValue() throws Exception {
        mockMvc.perform(post("/api/journeys/cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"distanceKm\": \"abc\", \"costPerKm\": 0.25}"))
                .andExpect(status().isBadRequest());
    }
}
