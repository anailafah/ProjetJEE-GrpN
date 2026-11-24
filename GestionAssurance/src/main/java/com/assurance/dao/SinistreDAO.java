package com.assurance.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.assurance.model.CommentaireSinistre;
import com.assurance.model.DocumentSinistre;
import com.assurance.model.Sinistre;
import com.assurance.model.StatutSinistre;
import com.assurance.model.Utilisateur;
import com.assurance.util.HibernateUtil;

public class SinistreDAO {

    // ------------------------------------------------------------------
    // Sinistres
    // ------------------------------------------------------------------

    public void ajouter(Sinistre sinistre) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(sinistre);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Erreur lors de l'ajout du sinistre", e);
        }
    }

    public void modifier(Sinistre sinistre) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(sinistre);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Erreur lors de la modification du sinistre", e);
        }
    }

    public Sinistre trouverParId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Sinistre.class, id);
        }
    }

    public Sinistre trouverParNumero(String numero) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Sinistre s WHERE s.numeroSinistre = :numero", Sinistre.class
            )
            .setParameter("numero", numero)
            .uniqueResult();
        }
    }

    // Tous les sinistres, les plus récents d'abord (agent / admin)
    public List<Sinistre> lister() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Sinistre s ORDER BY s.dateDeclaration DESC", Sinistre.class
            ).list();
        }
    }

    public List<Sinistre> listerParStatut(StatutSinistre statut) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Sinistre s WHERE s.statut = :statut ORDER BY s.dateDeclaration DESC",
                Sinistre.class
            )
            .setParameter("statut", statut)
            .list();
        }
    }

    // Sinistres d'un client (via le contrat concerné)
    public List<Sinistre> listerParClient(Long clientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Sinistre s WHERE s.contrat.client.id = :clientId "
                + "ORDER BY s.dateDeclaration DESC",
                Sinistre.class
            )
            .setParameter("clientId", clientId)
            .list();
        }
    }

    // Dernier numéro utilisé pour un préfixe donné, ex. "SIN-2026-"
    public String dernierNumero(String prefixe) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT MAX(s.numeroSinistre) FROM Sinistre s "
                + "WHERE s.numeroSinistre LIKE :prefixe",
                String.class
            )
            .setParameter("prefixe", prefixe + "%")
            .uniqueResult();
        }
    }

    // ------------------------------------------------------------------
    // Pièces justificatives
    // ------------------------------------------------------------------

    public void ajouterDocument(Long sinistreId, String nomFichier, String cheminFichier) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Sinistre sinistre = session.get(Sinistre.class, sinistreId);

            if (sinistre == null) {
                throw new IllegalArgumentException("Sinistre introuvable.");
            }

            DocumentSinistre document = new DocumentSinistre();
            document.setSinistre(sinistre);
            document.setNomFichier(nomFichier);
            document.setCheminFichier(cheminFichier);

            session.persist(document);

            transaction.commit();

        } catch (IllegalArgumentException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Erreur lors de l'ajout du document", e);
        }
    }

    public List<DocumentSinistre> listerDocuments(Long sinistreId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM DocumentSinistre d WHERE d.sinistre.id = :sinistreId "
                + "ORDER BY d.dateAjout",
                DocumentSinistre.class
            )
            .setParameter("sinistreId", sinistreId)
            .list();
        }
    }

    public DocumentSinistre trouverDocumentParId(Long documentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(DocumentSinistre.class, documentId);
        }
    }

    // ------------------------------------------------------------------
    // Commentaires
    // ------------------------------------------------------------------

    public void ajouterCommentaire(Long sinistreId, Long utilisateurId, String texte) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Sinistre sinistre = session.get(Sinistre.class, sinistreId);
            Utilisateur auteur = session.get(Utilisateur.class, utilisateurId);

            if (sinistre == null) {
                throw new IllegalArgumentException("Sinistre introuvable.");
            }

            if (auteur == null) {
                throw new IllegalArgumentException("Utilisateur introuvable.");
            }

            CommentaireSinistre commentaire = new CommentaireSinistre();
            commentaire.setSinistre(sinistre);
            commentaire.setAuteur(auteur);
            commentaire.setCommentaire(texte);

            session.persist(commentaire);

            transaction.commit();

        } catch (IllegalArgumentException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Erreur lors de l'ajout du commentaire", e);
        }
    }

    public List<CommentaireSinistre> listerCommentaires(Long sinistreId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM CommentaireSinistre c WHERE c.sinistre.id = :sinistreId "
                + "ORDER BY c.dateCommentaire",
                CommentaireSinistre.class
            )
            .setParameter("sinistreId", sinistreId)
            .list();
        }
    }
}
