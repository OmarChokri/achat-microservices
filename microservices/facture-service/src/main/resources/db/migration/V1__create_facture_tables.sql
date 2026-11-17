CREATE TABLE facture (
    id_facture BIGINT NOT NULL AUTO_INCREMENT,
    fournisseur_id BIGINT NOT NULL,
    montant_remise DECIMAL(12, 2) NOT NULL,
    montant_facture DECIMAL(12, 2) NOT NULL,
    date_creation_facture DATE NOT NULL,
    date_derniere_modification_facture DATE NOT NULL,
    archivee BOOLEAN NOT NULL,
    PRIMARY KEY (id_facture)
);

CREATE TABLE detail_facture (
    id_detail_facture BIGINT NOT NULL AUTO_INCREMENT,
    facture_id BIGINT NOT NULL,
    product_id VARCHAR(100) NOT NULL,
    qte_commandee INT NOT NULL,
    prix_unitaire DECIMAL(12, 2) NOT NULL,
    pourcentage_remise INT NOT NULL,
    prix_total_detail DECIMAL(12, 2) NOT NULL,
    montant_remise DECIMAL(12, 2) NOT NULL,
    PRIMARY KEY (id_detail_facture),
    CONSTRAINT fk_detail_facture_facture
        FOREIGN KEY (facture_id) REFERENCES facture (id_facture)
);
