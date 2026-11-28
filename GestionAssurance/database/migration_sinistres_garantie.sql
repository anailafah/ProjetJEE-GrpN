-- Relie chaque sinistre indemnisé à la garantie qui a servi à le rembourser.
-- A exécuter UNE FOIS sur une base existante (une base créée avec le nouveau schema.sql l'a déjà).

USE gestion_assurance;

ALTER TABLE sinistres
    ADD COLUMN garantie_id BIGINT NULL AFTER montant_indemnisation,
    ADD KEY garantie_id (garantie_id),
    ADD CONSTRAINT sinistres_ibfk_garantie FOREIGN KEY (garantie_id) REFERENCES garanties (id);
