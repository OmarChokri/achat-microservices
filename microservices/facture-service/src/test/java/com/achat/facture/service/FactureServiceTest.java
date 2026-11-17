package com.achat.facture.service;

import com.achat.facture.api.DetailFactureRequest;
import com.achat.facture.api.FactureRequest;
import com.achat.facture.domain.Facture;
import com.achat.facture.api.FactureResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FactureServiceTest {
    @Test
    void createCalculatesLineAndInvoiceTotals() {
        Facture facture = FactureService.buildFacture(new FactureRequest(
                42L,
                List.of(new DetailFactureRequest("p-1", 2, new BigDecimal("12.50"), 10))
        ), LocalDate.of(2026, 1, 1));
        FactureResponse response = FactureResponse.from(facture);

        assertEquals(new BigDecimal("22.50"), response.montantFacture());
        assertEquals(new BigDecimal("2.50"), response.montantRemise());
        assertEquals(new BigDecimal("22.50"), response.detailsFacture().get(0).prixTotalDetail());
        assertEquals(new BigDecimal("2.50"), response.detailsFacture().get(0).montantRemise());
    }

    @Test
    void archiveMarksInvoiceAndUpdatesModificationDate() {
        Facture facture = new Facture(42L, java.time.LocalDate.of(2026, 1, 1));

        facture.archive(LocalDate.of(2026, 1, 2));

        assertTrue(facture.isArchivee());
        assertFalse(facture.getDateDerniereModificationFacture().isBefore(facture.getDateCreationFacture()));
    }
}
