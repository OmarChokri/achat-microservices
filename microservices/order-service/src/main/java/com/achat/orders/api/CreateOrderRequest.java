package com.achat.orders.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateOrderRequest(
        @NotBlank String productId,
        @Positive int quantity
) {
}
