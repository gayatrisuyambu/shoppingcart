package com.example.shoppingcart.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PriceRequest(
        @NotNull(message = "items is required")
        @NotEmpty(message = "items must not be empty")
        List<String> items
) {
}
