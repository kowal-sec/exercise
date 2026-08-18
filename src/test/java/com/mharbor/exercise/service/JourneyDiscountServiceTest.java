package com.mharbor.exercise.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JourneyDiscountServiceTest {


    private final JourneyDiscountService service = new JourneyDiscountService();

    @Test
    void appliesFivePercentDiscountForTransX() {
        BigDecimal result = service.applyCustomerDiscount(new BigDecimal("100.00"), "TransX");
        assertEquals(0, result.compareTo(new BigDecimal("95.00")));
    }

    @Test
    void doesNotApplyDiscountForOtherCustomers() {
        BigDecimal result = service.applyCustomerDiscount(new BigDecimal("100.00"), "Acme");
        assertEquals(0, result.compareTo(new BigDecimal("100.00")));
    }

    @Test
    void doesNotApplyDiscountWhenCustomerIdIsNull() {
        BigDecimal result = service.applyCustomerDiscount(new BigDecimal("100.00"), null);
        assertEquals(0, result.compareTo(new BigDecimal("100.00")));
    }

    @Test
    void isCaseSensitiveAndDoesNotMatchLowercase() {
        BigDecimal result = service.applyCustomerDiscount(new BigDecimal("100.00"), "transx");
        assertEquals(0, result.compareTo(new BigDecimal("100.00")));
    }

    @Test
    void roundsHalfUpAfterApplyingDiscountToStep1Example() {
        BigDecimal result = service.applyCustomerDiscount(new BigDecimal("47.50"), "TransX");
        assertEquals(0, result.compareTo(new BigDecimal("45.13")));
    }

    @Test
    void throwsWhenTotalCostIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> service.applyCustomerDiscount(null, "TransX"));
    }

    @Test
    void throwsWhenTotalCostIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> service.applyCustomerDiscount(new BigDecimal("-10"), "TransX"));
    }

    @Test
    void doesNotApplyDiscountWhenCustomerIdIsBlank() {
        BigDecimal result = service.applyCustomerDiscount(new BigDecimal("100.00"), "");
        assertEquals(0, result.compareTo(new BigDecimal("100.00")));
    }
}
