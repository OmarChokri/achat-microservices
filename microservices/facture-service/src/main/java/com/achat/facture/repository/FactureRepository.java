package com.achat.facture.repository;

import com.achat.facture.domain.Facture;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FactureRepository extends JpaRepository<Facture, Long> {
    @EntityGraph(attributePaths = "detailsFacture")
    List<Facture> findAllByOrderByIdFactureDesc();

    @EntityGraph(attributePaths = "detailsFacture")
    Optional<Facture> findWithDetailsByIdFacture(Long idFacture);

    @EntityGraph(attributePaths = "detailsFacture")
    List<Facture> findByFournisseurIdOrderByIdFactureDesc(Long fournisseurId);
}
