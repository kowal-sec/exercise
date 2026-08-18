package com.mharbor.exercise.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class JourneyDiscountService {
    private static final String TRANSX_CUSTOMER_ID = "TransX";
    private static final BigDecimal TRANSX_DISCOUNT_RATE = BigDecimal.valueOf(0.05);

    public BigDecimal applyCustomerDiscount(BigDecimal totalCost, String customerId) {
        if (totalCost == null) {
            throw new IllegalArgumentException("totalCost must not be null");
        }
        if (totalCost.signum() < 0) {
            throw new IllegalArgumentException("totalCost must not be negative: " + totalCost);
        }
        if (!TRANSX_CUSTOMER_ID.equals(customerId)) {
            return totalCost;
        }

        return totalCost.multiply(BigDecimal.ONE.subtract(TRANSX_DISCOUNT_RATE))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
