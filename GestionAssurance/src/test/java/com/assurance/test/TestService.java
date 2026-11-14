package com.assurance.test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import org.hibernate.Session;

import com.assurance.util.HibernateUtil;
import com.assurance.model.Client;
import com.assurance.model.Contrat;
import com.assurance.model.Garantie;
import com.assurance.model.TypeAssurance;
import com.assurance.service.ContratService;
import com.assurance.service.GarantieService;
import com.assurance.service.TypeAssuranceService;

public class TestService {

    public static void main(String[] args) {

        TypeAssuranceService typeService = new TypeAssuranceService();
        GarantieService garantieService = new GarantieService();
        ContratService contratService = new ContratService();

        String suffixe = String.valueOf(System.currentTimeMillis());

        try {

            // =====================================
            // TEST TYPE ASSURANCE
            // =====================================

            System.out.println("\n========== TEST TYPE ASSURANCE ==========");

            TypeAssurance type = new TypeAssurance();
            type.setNom("TEST Automobile " + suffixe);
            type.setDescription("Type créé pour les tests");
            type.setDureeMois(12);
            type.setTarifBase(new BigDecimal("300.00"));

            typeService.ajouter(type);
            System.out.println("Ajout : OK (ID " + type.getId() + ")");

            System.out.println("Liste : "
                    + typeService.lister().size() + " type(s)");

            type.setTarifBase(new BigDecimal("350.00"));
            typeService.modifier(type);
            System.out.println("Modification : OK");

            // Test d'une validation incorrecte
            try {
                TypeAssurance invalide = new TypeAssurance();
                invalide.setNom("TEST invalide");
                invalide.setDureeMois(0);
                invalide.setTarifBase(new BigDecimal("100.00"));

                typeService.ajouter(invalide);

                throw new RuntimeException(
                    "La validation de la durée a échoué !");

            } catch (IllegalArgumentException e) {
                System.out.println("Validation durée : OK");
            }

            // =====================================
            // TEST GARANTIE
            // =====================================

            System.out.println("\n========== TEST GARANTIE ==========");

            Garantie garantie = new Garantie();
            garantie.setNom("TEST Garantie " + suffixe);
            garantie.setDescription("Garantie créée pour les tests");
            garantie.setMontantCouverture(
                    new BigDecimal("50000.00"));

            garantieService.ajouter(garantie);
            System.out.println("Ajout : OK (ID " + garantie.getId() + ")");

            System.out.println("Liste : "
                    + garantieService.lister().size() + " garantie(s)");

            garantie.setMontantCouverture(
                    new BigDecimal("75000.00"));

            garantieService.modifier(garantie);
            System.out.println("Modification : OK");

            // Test d'une validation incorrecte
            try {
                Garantie invalide = new Garantie();
                invalide.setNom("TEST Garantie invalide");
                invalide.setMontantCouverture(BigDecimal.ZERO);

                garantieService.ajouter(invalide);

                throw new RuntimeException(
                    "La validation du montant a échoué !");

            } catch (IllegalArgumentException e) {
                System.out.println("Validation montant : OK");
            }

            // =====================================
            // TEST CONTRAT
            // =====================================

            System.out.println("\n========== TEST CONTRAT ==========");

            // Récupérer le client de test existant (ID 1)
            Client client;

            try (Session session =
                    HibernateUtil.getSessionFactory().openSession()) {

                client = session.get(Client.class, 1L);
            }

            if (client == null) {
                throw new RuntimeException(
                    "Le client de test ID 1 est introuvable.");
            }

            Contrat contrat = new Contrat();

            contrat.setNumeroContrat("TEST-CONT-" + suffixe);
            contrat.setClient(client);
            contrat.setTypeAssurance(type);
            contrat.setDateDebut(LocalDate.now());
            contrat.setDateFin(LocalDate.now().plusMonths(12));
            contrat.setMontant(new BigDecimal("350.00"));
            contrat.setGaranties(new ArrayList<>());
            contrat.getGaranties().add(garantie);

            contratService.ajouter(contrat);

            System.out.println("Ajout : OK (ID "
                    + contrat.getId() + ")");

            System.out.println("Liste : "
                    + contratService.lister().size() + " contrat(s)");

            Contrat contratTrouve =
                    contratService.trouverParId(contrat.getId());

            if (contratTrouve == null) {
                throw new RuntimeException(
                    "Le contrat n'a pas été retrouvé.");
            }

            System.out.println("Recherche par ID : OK");

            contratTrouve.setMontant(new BigDecimal("400.00"));

            contratService.modifier(contratTrouve);

            System.out.println("Modification : OK");

            // =====================================
            // DESACTIVATION
            // =====================================

            System.out.println("\n========== DESACTIVATION ==========");

            typeService.desactiver(type.getId());
            System.out.println("Désactivation type : OK");

            garantieService.desactiver(garantie.getId());
            System.out.println("Désactivation garantie : OK");

            // =====================================
            // BILAN
            // =====================================

            System.out.println("\n========== BILAN ==========");
            System.out.println("Tous les tests sont terminés !");
            System.out.println("Les données de test sont conservées.");

        } catch (Exception e) {

            System.out.println("\nERREUR PENDANT LES TESTS :");
            e.printStackTrace();

        } finally {

            HibernateUtil.shutdown();
        }
    }
}