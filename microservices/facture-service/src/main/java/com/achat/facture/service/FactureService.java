package com.achat.facture.service;

import com.achat.facture.api.DetailFactureRequest;
import com.achat.facture.api.FactureRequest;
import com.achat.facture.api.FactureResponse;
import com.achat.facture.domain.DetailFacture;
import com.achat.facture.domain.Facture;
import com.achat.facture.repository.FactureRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class FactureService {
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private final FactureRepository factureRepository;

    public FactureService(FactureRepository factureRepository) {
        this.factureRepository = factureRepository;
    }

    @Transactional(readOnly = true)
    public List<FactureResponse> findAll() {
        return factureRepository.findAllByOrderByIdFactureDesc().stream()
                .map(FactureResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public FactureResponse findById(Long idFacture) {
        return FactureResponse.from(getFacture(idFacture));
    }

    @Transactional(readOnly = true)
    public List<FactureResponse> findByFournisseurId(Long fournisseurId) {
        return factureRepository.findByFournisseurIdOrderByIdFactureDesc(fournisseurId).stream()
                .map(FactureResponse::from)
                .toList();
    }

    public FactureResponse create(FactureRequest request) {
        Facture facture = buildFacture(request, LocalDate.now());
        return FactureResponse.from(factureRepository.save(facture));
    }

    static Facture buildFacture(FactureRequest request, LocalDate date) {
        Facture facture = new Facture(request.fournisseurId(), date);
        BigDecimal montantFacture = BigDecimal.ZERO.setScale(2);
        BigDecimal montantRemise = BigDecimal.ZERO.setScale(2);

        for (DetailFactureRequest requestDetail : request.detailsFacture()) {
            BigDecimal prixUnitaire = requestDetail.prixUnitaire().setScale(2, RoundingMode.HALF_UP);
            BigDecimal prixBrut = prixUnitaire
                    .multiply(BigDecimal.valueOf(requestDetail.qteCommandee()))
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal remiseDetail = prixBrut
                    .multiply(BigDecimal.valueOf(requestDetail.pourcentageRemise()))
                    .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
            BigDecimal prixNet = prixBrut.subtract(remiseDetail);

            facture.addDetail(new DetailFacture(
                    requestDetail.productId(),
                    requestDetail.qteCommandee(),
                    prixUnitaire,
                    requestDetail.pourcentageRemise(),
                    prixNet,
                    remiseDetail
            ));
            montantFacture = montantFacture.add(prixNet);
            montantRemise = montantRemise.add(remiseDetail);
        }

        facture.setTotals(montantFacture, montantRemise);
        return facture;
    }

    public void archive(Long idFacture) {
        Facture facture = getFacture(idFacture);
        facture.archive(LocalDate.now());
    }

    private Facture getFacture(Long idFacture) {
        return factureRepository.findWithDetailsByIdFacture(idFacture)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Facture not found"));
    }
}
