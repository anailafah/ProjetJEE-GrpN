package com.assurance.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.assurance.model.Contrat;
import com.assurance.util.HibernateUtil;

public class ContratDAO {

    public void ajouter(Contrat contrat) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(contrat);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Erreur lors de l'ajout du contrat", e);
        }
    }

    public List<Contrat> lister() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                "SELECT DISTINCT c FROM Contrat c " +
                "LEFT JOIN FETCH c.client " +
                "LEFT JOIN FETCH c.typeAssurance " +
                "LEFT JOIN FETCH c.garanties",
                Contrat.class
            ).list();

        }
    }

    // Récupérer uniquement les contrats d'un client
    public List<Contrat> listerParClient(Long clientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                "SELECT DISTINCT c FROM Contrat c " +
                "LEFT JOIN FETCH c.client " +
                "LEFT JOIN FETCH c.typeAssurance " +
                "LEFT JOIN FETCH c.garanties " +
                "WHERE c.client.id = :clientId",
                Contrat.class
            )
            .setParameter("clientId", clientId)
            .list();

        }
    }

    public Contrat trouverParId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                "SELECT DISTINCT c FROM Contrat c " +
                "LEFT JOIN FETCH c.client " +
                "LEFT JOIN FETCH c.typeAssurance " +
                "LEFT JOIN FETCH c.garanties " +
                "WHERE c.id = :id",
                Contrat.class
            )
            .setParameter("id", id)
            .uniqueResult();
        }
    }

    public void modifier(Contrat contrat) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(contrat);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Erreur lors de la modification du contrat", e);
        }
    }

    public void supprimer(Long id) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Contrat contrat = session.get(Contrat.class, id);

            if (contrat != null) {
                session.remove(contrat);
            }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Erreur lors de la suppression du contrat", e);
        }
    }

    public boolean numeroExiste(String numero) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long nombre = session.createQuery(
                "SELECT COUNT(c) FROM Contrat c WHERE c.numeroContrat = :numero",
                Long.class
            )
            .setParameter("numero", numero)
            .uniqueResult();

            return nombre != null && nombre > 0;
        }
    }

    public Contrat trouverParNumero(String numero) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Contrat c WHERE c.numeroContrat = :numero",
                Contrat.class
            )
            .setParameter("numero", numero)
            .uniqueResult();
        }
    }
    
 // Récupérer les demandes de contrats en attente
    public List<Contrat> listerDemandesEnAttente() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                "SELECT DISTINCT c FROM Contrat c " +
                "LEFT JOIN FETCH c.client " +
                "LEFT JOIN FETCH c.typeAssurance " +
                "LEFT JOIN FETCH c.garanties " +
                "WHERE c.statut = :statut",
                Contrat.class
            )
            .setParameter("statut", Contrat.EN_ATTENTE)
            .list();
        }
    }

    // Récupérer les demandes en attente d'un client
    public List<Contrat> listerDemandesEnAttenteParClient(Long clientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                "SELECT DISTINCT c FROM Contrat c " +
                "LEFT JOIN FETCH c.client " +
                "LEFT JOIN FETCH c.typeAssurance " +
                "LEFT JOIN FETCH c.garanties " +
                "WHERE c.client.id = :clientId " +
                "AND c.statut = :statut",
                Contrat.class
            )
            .setParameter("clientId", clientId)
            .setParameter("statut", Contrat.EN_ATTENTE)
            .list();
        }
    }
}