package com.mharbor.exercise.controller;

import com.mharbor.exercise.dto.JourneyCostRequest;
import com.mharbor.exercise.dto.JourneyCostResponse;
import com.mharbor.exercise.service.JourneyDiscountService;
import com.mharbor.exercise.service.JourneyPricingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/journeys")
public class JourneyController {

    private final JourneyPricingService pricingService;
    private final JourneyDiscountService discountService;


    public JourneyController(JourneyPricingService pricingService, JourneyDiscountService discountService) {
        this.pricingService = pricingService;
        this.discountService = discountService;
    }

    @PostMapping("/cost")
    public JourneyCostResponse calculateCost(@Valid @RequestBody JourneyCostRequest request){
        BigDecimal baseCost = pricingService.calculateCost(request.distanceKm(), request.costPerKm());
        BigDecimal finalCost = discountService.applyCustomerDiscount(baseCost, request.customerId());
        return new JourneyCostResponse(finalCost);
    }
}
