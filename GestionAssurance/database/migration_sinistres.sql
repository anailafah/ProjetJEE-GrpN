USE gestion_assurance;

ALTER TABLE sinistres
    ADD COLUMN type_sinistre VARCHAR(30) NOT NULL DEFAULT 'AUTRE' AFTER description;

ALTER TABLE sinistres
    MODIFY statut VARCHAR(30) NOT NULL DEFAULT 'DECLARE';

UPDATE sinistres SET statut = 'DECLARE' WHERE statut = 'EN_ATTENTE' OR statut IS NULL;