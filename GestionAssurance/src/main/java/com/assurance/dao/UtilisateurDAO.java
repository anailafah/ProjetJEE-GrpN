package com.assurance.dao;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.assurance.model.Administrateur;
import com.assurance.model.Agent;
import com.assurance.model.Client;
import com.assurance.model.Utilisateur;
import com.assurance.util.HibernateUtil;

/**
 * Accès aux données des utilisateurs (table "utilisateurs", héritage SINGLE_TABLE).
 * Aucune règle métier ici : validation, BCrypt, etc. sont dans UtilisateurService.
 */
public class UtilisateurDAO {

    /** Valeurs du discriminateur type_utilisateur -> classe d'entité. */
    private static final Map<String, Class<? extends Utilisateur>> TYPES = Map.of(
            "CLIENT", Client.class,
            "AGENT", Agent.class,
            "ADMIN", Administrateur.class);

    // ---------------------------------------------------------------
    // Écriture
    // ---------------------------------------------------------------

    public Utilisateur ajouter(Utilisateur utilisateur) {
        return enTransaction(session -> {
            session.persist(utilisateur);
            return utilisateur;
        });
    }

    public Utilisateur modifier(Utilisateur utilisateur) {
        return enTransaction(session -> session.merge(utilisateur));
    }
    
    public boolean modifierRole(Long id, String nouveauRole) {
        return enTransaction(session -> {
            int resultat = session.createNativeMutationQuery(
                    "UPDATE utilisateurs SET type_utilisateur = :role WHERE id = :id")
                    .setParameter("role", nouveauRole)
                    .setParameter("id", id)
                    .executeUpdate();

            return resultat > 0;
        });
    }

    /** @return true si l'utilisateur existe et a été activé */
    public boolean activer(Long id) {
        return changerStatut(id, true);
    }

    /** @return true si l'utilisateur existe et a été désactivé */
    public boolean desactiver(Long id) {
        return changerStatut(id, false);
    }

    private boolean changerStatut(Long id, boolean actif) {
        return enTransaction(session -> {
            Utilisateur u = session.get(Utilisateur.class, id);
            if (u == null) {
                return false;
            }
            u.setActif(actif);
            return true;
        });
    }

    // ---------------------------------------------------------------
    // Lecture
    // ---------------------------------------------------------------

    /** @return l'utilisateur, ou null s'il n'existe pas */
    public Utilisateur trouverParId(Long id) {
        return enLecture(session -> session.get(Utilisateur.class, id));
    }

    /** @return l'utilisateur, ou null s'il n'existe pas */
    public Utilisateur trouverParEmail(String email) {
        return enLecture(session -> session
                .createQuery("from Utilisateur u where u.email = :email", Utilisateur.class)
                .setParameter("email", email)
                .uniqueResultOptional()
                .orElse(null));
    }

    public boolean emailExiste(String email) {
        return enLecture(session -> session
                .createQuery("select count(u) from Utilisateur u where u.email = :email", Long.class)
                .setParameter("email", email)
                .uniqueResult() > 0);
    }

    public List<Utilisateur> lister() {
        return enLecture(session -> session
                .createQuery("from Utilisateur u order by u.nom, u.prenom", Utilisateur.class)
                .list());
    }

    /** @param type CLIENT, AGENT ou ADMIN (insensible à la casse) */
    public List<Utilisateur> listerParType(String type) {
        String entite = entiteDuType(type).getSimpleName();
        // entite vient de la Map ci-dessus (jamais de la saisie) : pas de risque d'injection
        return enLecture(session -> session
                .createQuery("from " + entite + " u order by u.nom, u.prenom", Utilisateur.class)
                .list());
    }

    public List<Utilisateur> listerClients() {
        return listerParType("CLIENT");
    }

    public List<Utilisateur> listerAgents() {
        return listerParType("AGENT");
    }

    public List<Utilisateur> listerAdministrateurs() {
        return listerParType("ADMIN");
    }

    /** Recherche par nom, prénom ou email (pour l'écran d'administration). */
    public List<Utilisateur> rechercher(String motCle) {
        String motif = "%" + motCle.toLowerCase() + "%";
        return enLecture(session -> session
                .createQuery("from Utilisateur u where lower(u.nom) like :m "
                        + "or lower(u.prenom) like :m or lower(u.email) like :m "
                        + "order by u.nom, u.prenom", Utilisateur.class)
                .setParameter("m", motif)
                .list());
    }

    // ---------------------------------------------------------------
    // Comptages (utiles pour le dashboard de P5 et la règle "dernier admin")
    // ---------------------------------------------------------------

    public long compterParType(String type) {
        String entite = entiteDuType(type).getSimpleName();
        return enLecture(session -> session
                .createQuery("select count(u) from " + entite + " u", Long.class)
                .uniqueResult());
    }

    public long compterActifsParType(String type) {
        String entite = entiteDuType(type).getSimpleName();
        return enLecture(session -> session
                .createQuery("select count(u) from " + entite + " u where u.actif = true", Long.class)
                .uniqueResult());
    }

    // ---------------------------------------------------------------
    // Outils internes : sessions toujours fermées, transactions annulées en cas d'erreur
    // ---------------------------------------------------------------

    private Class<? extends Utilisateur> entiteDuType(String type) {
        Class<? extends Utilisateur> classe = (type == null) ? null : TYPES.get(type.toUpperCase());
        if (classe == null) {
            throw new IllegalArgumentException("Type d'utilisateur inconnu : " + type);
        }
        return classe;
    }

    private <T> T enLecture(Function<Session, T> travail) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return travail.apply(session);
        }
    }

    private <T> T enTransaction(Function<Session, T> travail) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                T resultat = travail.apply(session);
                tx.commit();
                return resultat;
            } catch (RuntimeException e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            }
        }
    }
}
