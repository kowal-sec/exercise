package com.mharbor.exercise.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JourneyPricingServiceTest {

    private final JourneyPricingService service = new JourneyPricingService();

    @Test
    void chargesStandardRateForDistanceUnderThreshold() {
        BigDecimal result = service.calculateCost(new BigDecimal("50"), new BigDecimal("0.25"));
        assertEquals(0, result.compareTo(new BigDecimal("12.50")));
    }

    @Test
    void chargesStandardRateAtExactly100Km() {
        BigDecimal result = service.calculateCost(new BigDecimal("100"), new BigDecimal("0.25"));
        assertEquals(0, result.compareTo(new BigDecimal("25.00")));
    }

    @Test
    void appliesDiscountOnlyToDistanceAbove100Km() {
        BigDecimal result = service.calculateCost(new BigDecimal("200"), new BigDecimal("0.25"));
        assertEquals(0, result.compareTo(new BigDecimal("47.50")));
    }

    @Test
    void appliesDiscountToSmallExcessOver100Km() {
        BigDecimal result = service.calculateCost(new BigDecimal("100.10"), new BigDecimal("1"));
        assertEquals(0, result.compareTo(new BigDecimal("100.09")));
    }

    @Test
    void chargesStandardRateJustBelow100Km() {
        BigDecimal result = service.calculateCost(new BigDecimal("99.99"), new BigDecimal("1"));
        assertEquals(0, result.compareTo(new BigDecimal("99.99")));
    }

    @Test
    void returnsZeroForZeroDistance() {
        BigDecimal result = service.calculateCost(BigDecimal.ZERO, new BigDecimal("0.25"));
        assertEquals(0, result.compareTo(new BigDecimal("0.00")));
    }

    @Test
    void returnsZeroWhenCostPerKmIsZero() {
        BigDecimal result = service.calculateCost(new BigDecimal("200"), BigDecimal.ZERO);
        assertEquals(0, result.compareTo(new BigDecimal("0.00")));
    }

    @Test
    void roundsHalfUpToTwoDecimalPlaces() {
        BigDecimal result = service.calculateCost(new BigDecimal("3"), new BigDecimal("0.005"));
        assertEquals(0, result.compareTo(new BigDecimal("0.02")));
    }

    @Test
    void throwsWhenDistanceIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> service.calculateCost(new BigDecimal("-1"), new BigDecimal("0.25")));
    }

    @Test
    void throwsWhenCostPerKmIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> service.calculateCost(new BigDecimal("100"), new BigDecimal("-0.25")));
    }
}
