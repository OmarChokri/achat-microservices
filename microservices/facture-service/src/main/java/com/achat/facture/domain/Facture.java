package com.achat.facture.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facture")
public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_facture")
    private Long idFacture;

    @Column(name = "fournisseur_id", nullable = false)
    private Long fournisseurId;

    @Column(name = "montant_remise", nullable = false, precision = 12, scale = 2)
    private BigDecimal montantRemise;

    @Column(name = "montant_facture", nullable = false, precision = 12, scale = 2)
    private BigDecimal montantFacture;

    @Column(name = "date_creation_facture", nullable = false)
    private LocalDate dateCreationFacture;

    @Column(name = "date_derniere_modification_facture", nullable = false)
    private LocalDate dateDerniereModificationFacture;

    @Column(nullable = false)
    private boolean archivee;

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetailFacture> detailsFacture = new ArrayList<>();

    protected Facture() {
    }

    public Facture(Long fournisseurId, LocalDate dateCreationFacture) {
        this.fournisseurId = fournisseurId;
        this.dateCreationFacture = dateCreationFacture;
        this.dateDerniereModificationFacture = dateCreationFacture;
        this.montantRemise = BigDecimal.ZERO.setScale(2);
        this.montantFacture = BigDecimal.ZERO.setScale(2);
    }

    public void addDetail(DetailFacture detail) {
        detailsFacture.add(detail);
        detail.setFacture(this);
    }

    public void setTotals(BigDecimal montantFacture, BigDecimal montantRemise) {
        this.montantFacture = montantFacture;
        this.montantRemise = montantRemise;
    }

    public void archive(LocalDate date) {
        this.archivee = true;
        this.dateDerniereModificationFacture = date;
    }

    public Long getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(Long idFacture) {
        this.idFacture = idFacture;
    }

    public Long getFournisseurId() {
        return fournisseurId;
    }

    public BigDecimal getMontantRemise() {
        return montantRemise;
    }

    public BigDecimal getMontantFacture() {
        return montantFacture;
    }

    public LocalDate getDateCreationFacture() {
        return dateCreationFacture;
    }

    public LocalDate getDateDerniereModificationFacture() {
        return dateDerniereModificationFacture;
    }

    public boolean isArchivee() {
        return archivee;
    }

    public List<DetailFacture> getDetailsFacture() {
        return detailsFacture;
    }
}
