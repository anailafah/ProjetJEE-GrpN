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

            throw new RuntimeException("Erreur lors de l'ajout de la garantie.", e);
        }
    }

    // Lister toutes les garanties, actives et inactives
    public List<Garantie> lister() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Garantie g ORDER BY g.nom ASC",
                Garantie.class
            ).list();
        }
    }

    public Garantie trouverParId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Garantie.class, id);
        }
    }

    // Vérifier si un nom existe déjà, en ignorant la garantie modifiée
    public boolean nomExiste(String nom, Long idExclu) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            String hql;

            if (idExclu == null) {
                hql = "SELECT COUNT(g) FROM Garantie g "
                    + "WHERE LOWER(TRIM(g.nom)) = LOWER(TRIM(:nom))";
            } else {
                hql = "SELECT COUNT(g) FROM Garantie g "
                    + "WHERE LOWER(TRIM(g.nom)) = LOWER(TRIM(:nom)) "
                    + "AND g.id <> :id";
            }

            var query = session.createQuery(hql, Long.class);
            query.setParameter("nom", nom.trim());

            if (idExclu != null) {
                query.setParameter("id", idExclu);
            }

            return query.uniqueResult() > 0;
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

            throw new RuntimeException("Erreur lors de la modification de la garantie.", e);
        }
    }

}