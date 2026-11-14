USE gestion_assurance;

-- Types d'assurance
INSERT INTO types_assurance
(nom, description, duree_mois, prix_base, actif)
VALUES
('Assurance Automobile',
 'Protection des véhicules contre les accidents',
 12, 300.00, TRUE),

('Assurance Habitation',
 'Protection du logement et des biens',
 12, 200.00, TRUE),

('Assurance Santé',
 'Prise en charge des frais médicaux',
 12, 150.00, TRUE);

-- Garanties
INSERT INTO garanties
(nom, description, montant_maximal, actif)
VALUES
('Responsabilité civile',
 'Couvre les dommages causés à des tiers',
 100000.00, TRUE),

('Vol',
 'Couvre les pertes liées au vol',
 30000.00, TRUE),

('Incendie',
 'Couvre les dommages causés par un incendie',
 50000.00, TRUE);