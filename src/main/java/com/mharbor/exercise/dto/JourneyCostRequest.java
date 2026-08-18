package com.mharbor.exercise.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record JourneyCostRequest(
        @NotNull
        @DecimalMin(value = "0.0")
        BigDecimal distanceKm,
        @NotNull @DecimalMin(value = "0.0")
        BigDecimal costPerKm,
        String customerId
) {
}
