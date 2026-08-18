package com.mharbor.exercise.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class JourneyPricingService {
    private static final BigDecimal STANDARD_RATE_THRESHOLD_KM = BigDecimal.valueOf(100);
    private static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.10);

    public BigDecimal calculateCost(BigDecimal distanceKm, BigDecimal costPerKm){
        if (distanceKm.signum() < 0) {
            throw new IllegalArgumentException("distanceKm must not be negative: " + distanceKm);
        }
        if (costPerKm.signum() < 0) {
            throw new IllegalArgumentException("costPerKm must not be negative: " + costPerKm);
        }
        if (distanceKm.compareTo(STANDARD_RATE_THRESHOLD_KM) <= 0){
            return round(distanceKm.multiply(costPerKm));
        }

        BigDecimal distanceDistanceKm = distanceKm.subtract(STANDARD_RATE_THRESHOLD_KM);

        BigDecimal standardCost = STANDARD_RATE_THRESHOLD_KM.multiply(costPerKm);
        BigDecimal discoutedCost = distanceDistanceKm.multiply(costPerKm).multiply(BigDecimal.ONE.subtract(DISCOUNT_RATE));

        return round(standardCost.add(discoutedCost));
    }

    private BigDecimal round(BigDecimal value){
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
