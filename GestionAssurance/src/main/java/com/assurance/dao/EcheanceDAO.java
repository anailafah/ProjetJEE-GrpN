package com.assurance.dao;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.assurance.model.Echeance;
import com.assurance.util.HibernateUtil;

public class EcheanceDAO {

    public void ajouter(Echeance echeance) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(echeance);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Erreur lors de l'ajout de l'échéance", e);
        }
    }

    // Enregistre toutes les échéances d'un contrat dans UNE seule transaction :
    // soit tout est créé, soit rien (pas d'échéancier à moitié généré).
    public void ajouterTous(List<Echeance> echeances) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            for (Echeance echeance : echeances) {
                session.persist(echeance);
            }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Erreur lors de la génération des échéances", e);
        }
    }

    public Echeance trouverParId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT e FROM Echeance e JOIN FETCH e.contrat WHERE e.id = :id",
                Echeance.class
            ).setParameter("id", id).uniqueResult();
        }
    }

    public List<Echeance> listerParContrat(Long contratId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT e FROM Echeance e JOIN FETCH e.contrat c "
                + "WHERE c.id = :contratId ORDER BY e.dateEcheance",
                Echeance.class
            ).setParameter("contratId", contratId).list();
        }
    }

    // Toutes les échéances des contrats d'un client (espace client)
    public List<Echeance> listerParClient(Long clientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT e FROM Echeance e JOIN FETCH e.contrat c "
                + "WHERE c.client.id = :clientId "
                + "ORDER BY e.dateEcheance",
                Echeance.class
            ).setParameter("clientId", clientId).list();
        }
    }

    public List<Echeance> listerParStatut(String statut) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT e FROM Echeance e JOIN FETCH e.contrat "
                + "WHERE e.statut = :statut ORDER BY e.dateEcheance",
                Echeance.class
            ).setParameter("statut", statut).list();
        }
    }

    // Échéances dont la date est dépassée, sauf celles dont le statut est exclu
    // (payées, annulées : la liste est fournie par le service, qui porte les règles métier).
    public List<Echeance> listerImpayeesAvant(LocalDate date, List<String> statutsExclus) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT e FROM Echeance e JOIN FETCH e.contrat "
                + "WHERE e.statut NOT IN (:exclus) AND e.dateEcheance < :date "
                + "ORDER BY e.dateEcheance",
                Echeance.class
            ).setParameterList("exclus", statutsExclus)
             .setParameter("date", date)
             .list();
        }
    }

    // Nombre d'échéances d'un contrat à partir d'une date (période en cours)
    public long compterParContratDepuis(Long contratId, LocalDate depuis) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long total = session.createQuery(
                "SELECT COUNT(e) FROM Echeance e "
                + "WHERE e.contrat.id = :contratId AND e.dateEcheance >= :depuis",
                Long.class
            ).setParameter("contratId", contratId)
             .setParameter("depuis", depuis)
             .uniqueResult();

            return total == null ? 0L : total;
        }
    }

    // Change le statut des échéances d'un contrat à partir d'une date (résiliation)
    public int annulerFutures(Long contratId, LocalDate apartir,
            String statutActuel, String nouveauStatut) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            int modifiees = session.createMutationQuery(
                "UPDATE Echeance e SET e.statut = :nouveau "
                + "WHERE e.contrat.id = :contratId AND e.statut = :actuel "
                + "AND e.dateEcheance >= :date"
            ).setParameter("nouveau", nouveauStatut)
             .setParameter("contratId", contratId)
             .setParameter("actuel", statutActuel)
             .setParameter("date", apartir)
             .executeUpdate();

            transaction.commit();

            return modifiees;

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Erreur lors de l'annulation des échéances", e);
        }
    }

    public long compterParContrat(Long contratId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long total = session.createQuery(
                "SELECT COUNT(e) FROM Echeance e WHERE e.contrat.id = :contratId",
                Long.class
            ).setParameter("contratId", contratId).uniqueResult();

            return total == null ? 0L : total;
        }
    }

    // Change le statut de toutes les échéances en retard en une seule requête.
    // Retourne le nombre d'échéances modifiées.
    public int changerStatutAvant(LocalDate date, String statutActuel, String nouveauStatut) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            int modifiees = session.createMutationQuery(
                "UPDATE Echeance e SET e.statut = :nouveau "
                + "WHERE e.statut = :actuel AND e.dateEcheance < :date"
            ).setParameter("nouveau", nouveauStatut)
             .setParameter("actuel", statutActuel)
             .setParameter("date", date)
             .executeUpdate();

            transaction.commit();

            return modifiees;

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Erreur lors de la mise à jour des retards", e);
        }
    }

    public void modifier(Echeance echeance) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(echeance);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Erreur lors de la modification de l'échéance", e);
        }
    }
}
