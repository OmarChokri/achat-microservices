package com.achat.facture.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "detail_facture")
public class DetailFacture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detail_facture")
    private Long idDetailFacture;

    @Column(name = "product_id", nullable = false, length = 100)
    private String productId;

    @Column(name = "qte_commandee", nullable = false)
    private int qteCommandee;

    @Column(name = "prix_unitaire", nullable = false, precision = 12, scale = 2)
    private BigDecimal prixUnitaire;

    @Column(name = "pourcentage_remise", nullable = false)
    private int pourcentageRemise;

    @Column(name = "prix_total_detail", nullable = false, precision = 12, scale = 2)
    private BigDecimal prixTotalDetail;

    @Column(name = "montant_remise", nullable = false, precision = 12, scale = 2)
    private BigDecimal montantRemise;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "facture_id", nullable = false)
    private Facture facture;

    protected DetailFacture() {
    }

    public DetailFacture(
            String productId,
            int qteCommandee,
            BigDecimal prixUnitaire,
            int pourcentageRemise,
            BigDecimal prixTotalDetail,
            BigDecimal montantRemise
    ) {
        this.productId = productId;
        this.qteCommandee = qteCommandee;
        this.prixUnitaire = prixUnitaire;
        this.pourcentageRemise = pourcentageRemise;
        this.prixTotalDetail = prixTotalDetail;
        this.montantRemise = montantRemise;
    }

    void setFacture(Facture facture) {
        this.facture = facture;
    }

    public Long getIdDetailFacture() {
        return idDetailFacture;
    }

    public String getProductId() {
        return productId;
    }

    public int getQteCommandee() {
        return qteCommandee;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public int getPourcentageRemise() {
        return pourcentageRemise;
    }

    public BigDecimal getPrixTotalDetail() {
        return prixTotalDetail;
    }

    public BigDecimal getMontantRemise() {
        return montantRemise;
    }
}
