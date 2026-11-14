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
 

    public Contrat trouverParId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Contrat.class, id);
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
}