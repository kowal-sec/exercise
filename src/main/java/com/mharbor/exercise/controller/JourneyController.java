package com.mharbor.exercise.controller;

import com.mharbor.exercise.dto.JourneyCostRequest;
import com.mharbor.exercise.dto.JourneyCostResponse;
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

    public JourneyController(JourneyPricingService pricingService) {
        this.pricingService = pricingService;
    }

    @PostMapping("/cost")
    public JourneyCostResponse calculateCost(@Valid @RequestBody JourneyCostRequest request){
        BigDecimal totalCost = pricingService.calculateCost(request.distanceKm(), request.costPerKm());
        return new JourneyCostResponse(totalCost);
    }
}
