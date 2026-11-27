package com.assurance.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.assurance.model.TypeAssurance;
import com.assurance.util.HibernateUtil;

public class TypeAssuranceDAO {

    public void ajouter(TypeAssurance type) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(type);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erreur lors de l'ajout du type d'assurance", e);
        }
    }
    public List<TypeAssurance> lister() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM TypeAssurance", TypeAssurance.class
            ).list();
        }
    }

    public TypeAssurance trouverParId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(TypeAssurance.class, id);
        }
    }

    public void modifier(TypeAssurance type) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(type);
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

            TypeAssurance type = session.get(TypeAssurance.class, id);

            if (type != null) {
                session.remove(type);
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