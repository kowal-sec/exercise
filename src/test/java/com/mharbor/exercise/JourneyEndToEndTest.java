package com.mharbor.exercise;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JourneyEndToEndTest {


    @Autowired
    private MockMvc mockMvc;

    @Test
    void calculatesRealCostFor200KmAt025PerKm() throws Exception {
        mockMvc.perform(post("/api/journeys/cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"distanceKm\": 200, \"costPerKm\": 0.25}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCost").value(47.50));
    }
}
