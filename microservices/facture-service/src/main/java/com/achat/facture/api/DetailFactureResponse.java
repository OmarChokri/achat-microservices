package com.achat.facture.api;

import java.math.BigDecimal;

public record DetailFactureResponse(
        Long idDetailFacture,
        String productId,
        int qteCommandee,
        BigDecimal prixUnitaire,
        int pourcentageRemise,
        BigDecimal prixTotalDetail,
        BigDecimal montantRemise
) {
}
