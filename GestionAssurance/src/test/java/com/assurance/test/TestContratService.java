package com.assurance.test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.assurance.model.Client;
import com.assurance.model.Contrat;
import com.assurance.model.Garantie;
import com.assurance.model.TypeAssurance;
import com.assurance.service.ContratService;
import com.assurance.util.HibernateUtil;

public class TestContratService {

    public static void main(String[] args) {

        ContratService service = new ContratService();

        try {
            // Utiliser des données déjà présentes dans la base
            Client client = new Client();
            client.setId(1L);

            TypeAssurance type = new TypeAssurance();
            type.setId(2L);

            Garantie garantie = new Garantie();
            garantie.setId(2L);
            String numeroTest = "TEST-DOUBLON-" + System.currentTimeMillis();
            Contrat contrat = new Contrat();
            contrat.setNumeroContrat(numeroTest);
            contrat.setDateDebut(LocalDate.now());
            contrat.setDateFin(LocalDate.now().plusMonths(12));
            contrat.setMontant(new BigDecimal("300.00"));
            contrat.setClient(client);
            contrat.setTypeAssurance(type);

            ArrayList<Garantie> garanties = new ArrayList<>();
            garanties.add(garantie);
            contrat.setGaranties(garanties);

            // Premier ajout
            service.ajouter(contrat);
            System.out.println("Premier contrat ajouté avec succès !");

            // Deuxième ajout avec le même numéro
            Contrat doublon = new Contrat();
            doublon.setNumeroContrat(numeroTest);
            doublon.setDateDebut(LocalDate.now());
            doublon.setDateFin(LocalDate.now().plusMonths(12));
            doublon.setMontant(new BigDecimal("300.00"));
            doublon.setClient(client);
            doublon.setTypeAssurance(type);
            doublon.setGaranties(garanties);
         // Test de modification du premier contrat
            Contrat contratModifie = service.trouverParId(contrat.getId());

            if (contratModifie != null) {

                contratModifie.setMontant(new BigDecimal("450.00"));

                service.modifier(contratModifie);

                Contrat resultat = service.trouverParId(contrat.getId());

                if (resultat.getMontant().compareTo(new BigDecimal("450.00")) == 0) {
                    System.out.println("Test réussi : contrat modifié avec succès !");
                } else {
                    System.out.println("ERREUR : modification non enregistrée !");
                }
            }

            try {
                service.ajouter(doublon);
                System.out.println("ERREUR : le doublon a été accepté !");
            } catch (IllegalArgumentException e) {
                System.out.println("Test réussi : " + e.getMessage());
            }
         // Test : refus d'un contrat pour un client désactivé

         // 1. Désactiver réellement le client dans MySQL
         try (Session session = HibernateUtil.getSessionFactory().openSession()) {
             Transaction transaction = session.beginTransaction();

             Client clientBDD = session.get(Client.class, 1L);
             clientBDD.setActif(false);

             transaction.commit();
         }

         try {
             // 2. Préparer un contrat avec ce client
             Client clientDesactive = new Client();
             clientDesactive.setId(1L);

             Contrat contratClientDesactive = new Contrat();
             contratClientDesactive.setNumeroContrat(
                 "TEST-CLIENT-DESACTIVE-" + System.currentTimeMillis()
             );
             contratClientDesactive.setDateDebut(LocalDate.now());
             contratClientDesactive.setDateFin(LocalDate.now().plusMonths(12));
             contratClientDesactive.setMontant(new BigDecimal("300.00"));
             contratClientDesactive.setClient(clientDesactive);
             contratClientDesactive.setTypeAssurance(type);
             contratClientDesactive.setGaranties(garanties);

             service.ajouter(contratClientDesactive);

             System.out.println("ERREUR : un client désactivé a été accepté !");

         } catch (IllegalArgumentException e) {
             System.out.println("Test réussi : " + e.getMessage());

         } finally {
             // 3. Réactiver le client dans tous les cas
             try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                 Transaction transaction = session.beginTransaction();

                 Client clientBDD = session.get(Client.class, 1L);
                 clientBDD.setActif(true);

                 transaction.commit();
             }
         }
         
      // Test : refus d'un type d'assurance désactivé

      // 1. Désactiver temporairement le type d'assurance
      try (Session session = HibernateUtil.getSessionFactory().openSession()) {
          Transaction transaction = session.beginTransaction();

          TypeAssurance typeBDD = session.get(TypeAssurance.class, 2L);
          typeBDD.setActif(false);

          transaction.commit();
      }

      try {
          // 2. Préparer un contrat avec ce type désactivé
          TypeAssurance typeDesactive = new TypeAssurance();
          typeDesactive.setId(2L);

          Contrat contratTypeDesactive = new Contrat();
          contratTypeDesactive.setNumeroContrat(
              "TEST-TYPE-DESACTIVE-" + System.currentTimeMillis()
          );
          contratTypeDesactive.setDateDebut(LocalDate.now());
          contratTypeDesactive.setDateFin(LocalDate.now().plusMonths(12));
          contratTypeDesactive.setMontant(new BigDecimal("300.00"));
          contratTypeDesactive.setClient(client);
          contratTypeDesactive.setTypeAssurance(typeDesactive);
          contratTypeDesactive.setGaranties(garanties);

          service.ajouter(contratTypeDesactive);

          System.out.println("ERREUR : un type désactivé a été accepté !");

      } catch (IllegalArgumentException e) {
          System.out.println("Test réussi : " + e.getMessage());

      } finally {
          // 3. Réactiver le type d'assurance
          try (Session session = HibernateUtil.getSessionFactory().openSession()) {
              Transaction transaction = session.beginTransaction();

              TypeAssurance typeBDD = session.get(TypeAssurance.class, 2L);
              typeBDD.setActif(true);

              transaction.commit();
          }
      }
      
   // Test : refus d'une garantie désactivée

   // 1. Désactiver temporairement la garantie
   try (Session session = HibernateUtil.getSessionFactory().openSession()) {
       Transaction transaction = session.beginTransaction();

       Garantie garantieBDD = session.get(Garantie.class, 2L);
       garantieBDD.setActif(false);

       transaction.commit();
   }

   try {
       // 2. Préparer un contrat avec la garantie désactivée
       Garantie garantieDesactivee = new Garantie();
       garantieDesactivee.setId(2L);

       ArrayList<Garantie> garantiesDesactivees = new ArrayList<>();
       garantiesDesactivees.add(garantieDesactivee);

       Contrat contratGarantieDesactivee = new Contrat();
       contratGarantieDesactivee.setNumeroContrat(
           "TEST-GARANTIE-DESACTIVE-" + System.currentTimeMillis()
       );
       contratGarantieDesactivee.setDateDebut(LocalDate.now());
       contratGarantieDesactivee.setDateFin(LocalDate.now().plusMonths(12));
       contratGarantieDesactivee.setMontant(new BigDecimal("300.00"));
       contratGarantieDesactivee.setClient(client);
       contratGarantieDesactivee.setTypeAssurance(type);
       contratGarantieDesactivee.setGaranties(garantiesDesactivees);

       service.ajouter(contratGarantieDesactivee);

       System.out.println("ERREUR : une garantie désactivée a été acceptée !");

   } catch (IllegalArgumentException e) {
       System.out.println("Test réussi : " + e.getMessage());

   } finally {
       // 3. Réactiver la garantie
       try (Session session = HibernateUtil.getSessionFactory().openSession()) {
           Transaction transaction = session.beginTransaction();

           Garantie garantieBDD = session.get(Garantie.class, 2L);
           garantieBDD.setActif(true);

           transaction.commit();
       }
   }
   
// Test : refus d'une date de fin antérieure à la date de début

try {
    Contrat contratDatesInvalides = new Contrat();

    contratDatesInvalides.setNumeroContrat(
        "TEST-DATES-" + System.currentTimeMillis()
    );

    contratDatesInvalides.setDateDebut(LocalDate.now());
    contratDatesInvalides.setDateFin(LocalDate.now().minusMonths(1));

    contratDatesInvalides.setMontant(new BigDecimal("300.00"));
    contratDatesInvalides.setClient(client);
    contratDatesInvalides.setTypeAssurance(type);
    contratDatesInvalides.setGaranties(garanties);

    service.ajouter(contratDatesInvalides);

    System.out.println("ERREUR : des dates invalides ont été acceptées !");

} catch (IllegalArgumentException e) {
    System.out.println("Test réussi : " + e.getMessage());
}

//Test : refus d'un montant nul

try {
 Contrat contratMontantInvalide = new Contrat();

 contratMontantInvalide.setNumeroContrat(
     "TEST-MONTANT-" + System.currentTimeMillis()
 );

 contratMontantInvalide.setDateDebut(LocalDate.now());
 contratMontantInvalide.setDateFin(LocalDate.now().plusMonths(12));

 contratMontantInvalide.setMontant(BigDecimal.ZERO);
 contratMontantInvalide.setClient(client);
 contratMontantInvalide.setTypeAssurance(type);
 contratMontantInvalide.setGaranties(garanties);

 service.ajouter(contratMontantInvalide);

 System.out.println("ERREUR : un montant nul a été accepté !");

} catch (IllegalArgumentException e) {
 System.out.println("Test réussi : " + e.getMessage());
}

//Test : refus d'un contrat sans garantie

try {
 Contrat contratSansGarantie = new Contrat();

 contratSansGarantie.setNumeroContrat(
     "TEST-SANS-GARANTIE-" + System.currentTimeMillis()
 );

 contratSansGarantie.setDateDebut(LocalDate.now());
 contratSansGarantie.setDateFin(LocalDate.now().plusMonths(12));
 contratSansGarantie.setMontant(new BigDecimal("300.00"));
 contratSansGarantie.setClient(client);
 contratSansGarantie.setTypeAssurance(type);

 contratSansGarantie.setGaranties(new ArrayList<>());

 service.ajouter(contratSansGarantie);

 System.out.println("ERREUR : un contrat sans garantie a été accepté !");

} catch (IllegalArgumentException e) {
 System.out.println("Test réussi : " + e.getMessage());
}

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	
            HibernateUtil.shutdown();
        }
        
    }
    
}