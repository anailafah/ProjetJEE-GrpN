package com.assurance.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.assurance.model.Echeance;
import com.assurance.model.Paiement;
import com.assurance.util.HibernateUtil;

public class PaiementDAO {

    // Charge le paiement avec son échéance et son contrat : les JSP peuvent
    // afficher paiement.echeance.contrat.numeroContrat sans erreur de chargement
    // différé (la session est déjà fermée quand la JSP s'exécute).
    private static final String SELECT_PAIEMENT =
        "SELECT p FROM Paiement p "
        + "JOIN FETCH p.echeance e "
        + "JOIN FETCH e.contrat c ";

    private static final String ORDRE = " ORDER BY p.datePaiement DESC";

    // Enregistre (ou met à jour) un paiement ET, si nouveauStatutEcheance n'est pas
    // null, change le statut de son échéance dans la MÊME transaction :
    // on ne peut pas avoir un paiement enregistré avec une échéance restée impayée.
    public void enregistrer(Paiement paiement, String nouveauStatutEcheance) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            if (paiement.getId() == null) {
                session.persist(paiement);
            } else {
                paiement = session.merge(paiement);
            }

            if (nouveauStatutEcheance != null) {
                Echeance echeance = session.get(
                    Echeance.class, paiement.getEcheance().getId());

                if (echeance == null) {
                    throw new IllegalStateException("Échéance introuvable.");
                }

                // Objet géré par la session : la modification est enregistrée au commit.
                echeance.setStatut(nouveauStatutEcheance);
            }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Erreur lors de l'enregistrement du paiement", e);
        }
    }

    public Paiement trouverParId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                SELECT_PAIEMENT + "WHERE p.id = :id",
                Paiement.class
            ).setParameter("id", id).uniqueResult();
        }
    }

    // Historique complet, du plus récent au plus ancien
    public List<Paiement> lister() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                SELECT_PAIEMENT + ORDRE,
                Paiement.class
            ).list();
        }
    }

    public List<Paiement> listerParStatut(String statut) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                SELECT_PAIEMENT + "WHERE p.statut = :statut" + ORDRE,
                Paiement.class
            ).setParameter("statut", statut).list();
        }
    }

    public List<Paiement> listerParEcheance(Long echeanceId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                SELECT_PAIEMENT + "WHERE e.id = :echeanceId" + ORDRE,
                Paiement.class
            ).setParameter("echeanceId", echeanceId).list();
        }
    }

    // Historique des paiements d'un contrat
    public List<Paiement> listerParContrat(Long contratId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                SELECT_PAIEMENT + "WHERE c.id = :contratId" + ORDRE,
                Paiement.class
            ).setParameter("contratId", contratId).list();
        }
    }

    // Historique des paiements d'un client (espace client)
    public List<Paiement> listerParClient(Long clientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                SELECT_PAIEMENT + "WHERE c.client.id = :clientId" + ORDRE,
                Paiement.class
            ).setParameter("clientId", clientId).list();
        }
    }

    // Somme des paiements d'une échéance ayant le statut donné
    // (le service passe "EFFECTUE" pour obtenir le montant réellement payé).
    public BigDecimal sommeParEcheanceEtStatut(Long echeanceId, String statut) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            BigDecimal total = session.createQuery(
                "SELECT SUM(p.montant) FROM Paiement p "
                + "WHERE p.echeance.id = :echeanceId AND p.statut = :statut",
                BigDecimal.class
            ).setParameter("echeanceId", echeanceId)
             .setParameter("statut", statut)
             .uniqueResult();

            // SUM renvoie null quand il n'y a aucun paiement
            return total == null ? BigDecimal.ZERO : total;
        }
    }
}
