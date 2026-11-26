package com.assurance.test;

import java.math.BigDecimal;
import java.util.List;

import com.assurance.model.Contrat;
import com.assurance.model.Echeance;
import com.assurance.model.Paiement;
import com.assurance.service.ContratService;
import com.assurance.service.EcheanceService;
import com.assurance.service.PaiementService;

// Test manuel de la partie Paiements / Échéances.
// Prérequis : le contrat de test (id 100) créé par le script SQL donné à côté.
// Lancer : clic droit > Run As > Java Application.
public class TestPaiementEcheance {

    private static final Long CONTRAT_ID = 100L;

    public static void main(String[] args) {

        ContratService contratService = new ContratService();
        EcheanceService echeanceService = new EcheanceService();
        PaiementService paiementService = new PaiementService();

        try {
            Contrat contrat = contratService.trouverParId(CONTRAT_ID);

            if (contrat == null) {
                System.out.println("Contrat " + CONTRAT_ID + " introuvable : lancez d'abord le script SQL.");
                return;
            }

            // 1. Génération de l'échéancier (une seule fois par contrat)
            if (echeanceService.listerParContrat(CONTRAT_ID).isEmpty()) {
                echeanceService.genererEcheances(contrat);
                System.out.println("=== Échéances générées ===");
            } else {
                System.out.println("=== Échéances déjà présentes (génération ignorée) ===");
            }

            // 2. Une seconde génération doit être refusée
            try {
                echeanceService.genererEcheances(contrat);
                System.out.println("ERREUR : la seconde génération aurait dû être refusée");
            } catch (IllegalArgumentException e) {
                System.out.println("OK, seconde génération refusée : " + e.getMessage());
            }

            // 3. Retards : les échéances passées non payées passent EN_RETARD
            int retards = echeanceService.gererRetards();
            System.out.println("Échéances passées en retard : " + retards);

            List<Echeance> echeances = echeanceService.listerParContrat(CONTRAT_ID);
            afficherEcheances(echeances);

            // On travaille sur la première échéance non payée
            Echeance cible = echeances.stream()
                    .filter(e -> !EcheanceService.STATUT_PAYEE.equals(e.getStatut()))
                    .findFirst()
                    .orElse(null);

            if (cible == null) {
                System.out.println("Toutes les échéances sont payées, rien à tester.");
                return;
            }

            Long id = cible.getId();
            System.out.println("\n=== Tests sur l'échéance " + id + " (" + cible.getMontant() + " €) ===");

            // 4. Paiement trop élevé : refusé
            try {
                paiementService.enregistrerPaiement(id, cible.getMontant().add(BigDecimal.ONE), "CARTE", null);
                System.out.println("ERREUR : un paiement trop élevé aurait dû être refusé");
            } catch (IllegalArgumentException e) {
                System.out.println("OK, montant trop élevé refusé : " + e.getMessage());
            }

            // 5. Mode de paiement invalide : refusé
            try {
                paiementService.enregistrerPaiement(id, BigDecimal.TEN, "BITCOIN", null);
                System.out.println("ERREUR : un mode invalide aurait dû être refusé");
            } catch (IllegalArgumentException e) {
                System.out.println("OK, mode invalide refusé : " + e.getMessage());
            }

            // 6. Paiement partiel : l'échéance ne doit PAS passer à PAYEE
            BigDecimal moitie = cible.getMontant().divide(new BigDecimal("2"));
            paiementService.enregistrerPaiement(id, moitie, "CARTE", null);
            System.out.println("Paiement partiel de " + moitie + " €, reste : "
                    + paiementService.resteAPayer(id)
                    + " €, statut : " + echeanceService.trouverParId(id).getStatut());

            // 7. Paiement en attente (virement annoncé) puis validation
            BigDecimal reste = paiementService.resteAPayer(id);
            Paiement enAttente = paiementService.declarerPaiementEnAttente(id, reste, "VIREMENT", "VIR-TEST");
            System.out.println("Virement en attente déclaré, statut échéance : "
                    + echeanceService.trouverParId(id).getStatut()
                    + " (ne doit pas être PAYEE)");

            paiementService.validerPaiement(enAttente.getId());
            System.out.println("Virement validé, reste : " + paiementService.resteAPayer(id)
                    + " €, statut : " + echeanceService.trouverParId(id).getStatut()
                    + " (doit être PAYEE)");

            // 8. Échéance soldée : un nouveau paiement est refusé
            try {
                paiementService.enregistrerPaiement(id, BigDecimal.ONE, "CARTE", null);
                System.out.println("ERREUR : payer une échéance soldée aurait dû être refusé");
            } catch (IllegalArgumentException e) {
                System.out.println("OK, échéance déjà payée : " + e.getMessage());
            }

            // 9. Listes et historique
            System.out.println("\n=== Paiements du contrat ===");
            for (Paiement p : paiementService.historiqueParContrat(CONTRAT_ID)) {
                System.out.println(p.getDatePaiement() + " | " + p.getMontant() + " € | "
                        + p.getModePaiement() + " | " + p.getStatut() + " | "
                        + p.getReferencePaiement() + " | contrat "
                        + p.getEcheance().getContrat().getNumeroContrat());
            }

            System.out.println("\nPaiements effectués : " + paiementService.listerEffectues().size());
            System.out.println("Paiements en attente : " + paiementService.listerEnAttente().size());
            System.out.println("Échéances impayées (date dépassée) : "
                    + echeanceService.detecterEcheancesImpayees().size());

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(0); // ferme les connexions Hibernate restées ouvertes
    }

    private static void afficherEcheances(List<Echeance> echeances) {
        System.out.println("\n=== Échéancier ===");
        for (Echeance e : echeances) {
            System.out.println(e.getId() + " | " + e.getDateEcheance() + " | "
                    + e.getMontant() + " € | " + e.getStatut());
        }
    }
}
