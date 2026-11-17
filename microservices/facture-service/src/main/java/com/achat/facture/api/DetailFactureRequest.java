package com.achat.facture.api;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DetailFactureRequest(
        @NotBlank String productId,
        @Positive int qteCommandee,
        @NotNull @DecimalMin("0.00") BigDecimal prixUnitaire,
        @Min(0) @Max(100) int pourcentageRemise
) {
}
