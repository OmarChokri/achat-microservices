package com.achat.facture.api;

import com.achat.facture.domain.Facture;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FactureResponse(
        Long idFacture,
        Long fournisseurId,
        BigDecimal montantRemise,
        BigDecimal montantFacture,
        LocalDate dateCreationFacture,
        LocalDate dateDerniereModificationFacture,
        boolean archivee,
        List<DetailFactureResponse> detailsFacture
) {
    public static FactureResponse from(Facture facture) {
        List<DetailFactureResponse> details = facture.getDetailsFacture().stream()
                .map(detail -> new DetailFactureResponse(
                        detail.getIdDetailFacture(),
                        detail.getProductId(),
                        detail.getQteCommandee(),
                        detail.getPrixUnitaire(),
                        detail.getPourcentageRemise(),
                        detail.getPrixTotalDetail(),
                        detail.getMontantRemise()
                ))
                .toList();

        return new FactureResponse(
                facture.getIdFacture(),
                facture.getFournisseurId(),
                facture.getMontantRemise(),
                facture.getMontantFacture(),
                facture.getDateCreationFacture(),
                facture.getDateDerniereModificationFacture(),
                facture.isArchivee(),
                details
        );
    }
}
