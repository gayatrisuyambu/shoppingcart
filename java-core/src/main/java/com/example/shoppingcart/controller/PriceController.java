package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.PriceBreakDown;
import com.example.shoppingcart.model.PriceRequest;
import com.example.shoppingcart.service.PriceCalculator;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api")
public class PriceController {
    private final PriceCalculator priceCalculator = new PriceCalculator();

    @GetMapping("/health")
    public String health(){
        return "OK";
    }

    @PostMapping(value = "/price",consumes = "application/json", produces = "application/json")
    public PriceBreakDown getPriceBreakDown(@Valid @RequestBody PriceRequest priceRequest){
        return  priceCalculator.calculatePriceBreakDown(priceRequest);
    }
}
