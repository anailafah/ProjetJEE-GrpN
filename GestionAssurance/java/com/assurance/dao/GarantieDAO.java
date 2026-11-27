package com.assurance.dao;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.assurance.model.Garantie;
import com.assurance.util.HibernateUtil;

public class GarantieDAO {

    public void ajouter(Garantie garantie) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(garantie);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Erreur lors de l'ajout de la garantie", e);
        }
    }

    // Lister toutes les garanties
    public List<Garantie> lister() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Garantie", Garantie.class
            ).list();
        }
    }

    public Garantie trouverParId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Garantie.class, id);
        }
    }

    public void modifier(Garantie garantie) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(garantie);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Erreur lors de la modification", e);
        }
    }

    public void supprimer(Long id) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Garantie garantie = session.get(Garantie.class, id);

            if (garantie != null) {
                session.remove(garantie);
            }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Erreur lors de la suppression", e);
        }
    }
}