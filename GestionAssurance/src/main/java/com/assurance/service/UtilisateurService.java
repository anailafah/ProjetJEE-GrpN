package com.assurance.service;

import java.time.LocalDate;
import java.util.regex.Pattern;

import org.hibernate.exception.ConstraintViolationException;
import org.mindrot.jbcrypt.BCrypt;

import com.assurance.dao.UtilisateurDAO;
import com.assurance.model.Administrateur;
import com.assurance.model.Agent;
import com.assurance.model.Client;
import com.assurance.model.Utilisateur;

/**
 * Règles métier des utilisateurs : validation, hachage BCrypt, création des comptes,
 * authentification, activation/désactivation.
 *
 * Le rôle d'un utilisateur est fixé à sa création (CLIENT, AGENT ou ADMIN) et aucune méthode
 * ne permet de le modifier : un utilisateur ne peut donc pas changer son propre rôle.
 */
public class UtilisateurService {

    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern TELEPHONE = Pattern.compile("^[0-9+][0-9 .-]{5,19}$");

    private static final int MDP_MIN = 8;
    /** BCrypt ignore tout ce qui dépasse 72 caractères : on le refuse explicitement. */
    private static final int MDP_MAX = 72;

    private final UtilisateurDAO dao;

    public UtilisateurService() {
        this(new UtilisateurDAO());
    }

    public UtilisateurService(UtilisateurDAO dao) {
        this.dao = dao;
    }

    // ---------------------------------------------------------------
    // Création des comptes
    // ---------------------------------------------------------------

    /** Inscription publique : crée toujours un compte CLIENT, actif. */
    public Client creerClient(String nom, String prenom, String email, String motDePasse,
                              String telephone, String adresse, LocalDate dateNaissance) {
        Client client = new Client();
        remplirChampsCommuns(client, nom, prenom, email, motDePasse, telephone);
        client.setAdresse(optionnel(adresse, 255, "L'adresse"));
        client.setDateNaissance(verifierDateNaissance(dateNaissance));
        enregistrer(client);
        return client;
    }

    /** À réserver à un administrateur connecté (le contrôle se fait dans le servlet / le filtre). */
    public Agent creerAgent(String nom, String prenom, String email, String motDePasse,
                            String telephone, String matricule, String service) {
        Agent agent = new Agent();
        remplirChampsCommuns(agent, nom, prenom, email, motDePasse, telephone);
        agent.setMatricule(obligatoire(matricule, 50, "Le matricule"));
        agent.setService(obligatoire(service, 100, "Le service"));
        enregistrer(agent);
        return agent;
    }

    /** À réserver à un administrateur connecté (le contrôle se fait dans le servlet / le filtre). */
    public Administrateur creerAdministrateur(String nom, String prenom, String email, String motDePasse,
                                              String telephone, String niveauAcces) {
        Administrateur admin = new Administrateur();
        remplirChampsCommuns(admin, nom, prenom, email, motDePasse, telephone);
        enregistrer(admin);
        return admin;
    }

    // ---------------------------------------------------------------
    // Authentification
    // ---------------------------------------------------------------

    /**
     * Vérifie l'email et le mot de passe. Même message si l'email est inconnu ou si le mot de passe
     * est faux, pour ne pas révéler quels emails existent.
     */
    public Utilisateur authentifier(String email, String motDePasse) {
        if (estVide(email) || estVide(motDePasse)) {
            throw new ServiceException("L'email et le mot de passe sont obligatoires.");
        }
        Utilisateur u = dao.trouverParEmail(normaliser(email));
        if (u == null || !motDePasseCorrect(motDePasse, u.getMotDePasse())) {
            throw new ServiceException("Email ou mot de passe incorrect.");
        }
        if (!u.isActif()) {
            throw new ServiceException("Ce compte est désactivé. Contactez un administrateur.");
        }
        return u;
    }

    // ---------------------------------------------------------------
    // Modification de compte
    // ---------------------------------------------------------------

    /** Modifie uniquement le nom, le prénom et le téléphone (ni l'email, ni le rôle). */
    public Utilisateur modifierInformationsPersonnelles(Long id, String nom, String prenom, String telephone) {
        Utilisateur u = chargerOuErreur(id);
        u.setNom(obligatoire(nom, 100, "Le nom"));
        u.setPrenom(obligatoire(prenom, 100, "Le prénom"));
        u.setTelephone(verifierTelephone(telephone));
        return dao.modifier(u);
    }

    public void changerMotDePasse(Long id, String ancienMotDePasse, String nouveauMotDePasse) {
        Utilisateur u = chargerOuErreur(id);
        if (estVide(ancienMotDePasse) || !motDePasseCorrect(ancienMotDePasse, u.getMotDePasse())) {
            throw new ServiceException("L'ancien mot de passe est incorrect.");
        }
        validerMotDePasse(nouveauMotDePasse);
        u.setMotDePasse(hacher(nouveauMotDePasse));
        dao.modifier(u);
    }

    public void activerCompte(Long id) {
        chargerOuErreur(id);
        dao.activer(id);
    }

    public void desactiverCompte(Long id) {
        Utilisateur u = chargerOuErreur(id);
        if (!u.isActif()) {
            return; // déjà désactivé
        }
        if (u instanceof Administrateur && dao.compterActifsParType("ADMIN") <= 1) {
            throw new ServiceException("Impossible de désactiver le dernier administrateur actif.");
        }
        dao.desactiver(id);
    }

    // ---------------------------------------------------------------
    // Validation (méthodes internes)
    // ---------------------------------------------------------------

    private void remplirChampsCommuns(Utilisateur u, String nom, String prenom, String email,
                                      String motDePasse, String telephone) {
        u.setNom(obligatoire(nom, 100, "Le nom"));
        u.setPrenom(obligatoire(prenom, 100, "Le prénom"));
        u.setEmail(verifierEmailDisponible(email));
        validerMotDePasse(motDePasse);
        u.setMotDePasse(hacher(motDePasse));
        u.setTelephone(verifierTelephone(telephone));
        u.setActif(true); // statut initial : compte actif
    }

    private String verifierEmailDisponible(String email) {
        if (estVide(email)) {
            throw new ServiceException("L'email est obligatoire.");
        }
        String e = normaliser(email);
        if (e.length() > 150 || !EMAIL.matcher(e).matches()) {
            throw new ServiceException("Le format de l'email est invalide.");
        }
        if (dao.emailExiste(e)) {
            throw new ServiceException("Cet email est déjà utilisé.");
        }
        return e;
    }

    private void validerMotDePasse(String mdp) {
        if (mdp == null || mdp.length() < MDP_MIN) {
            throw new ServiceException("Le mot de passe doit contenir au moins " + MDP_MIN + " caractères.");
        }
        if (mdp.length() > MDP_MAX) {
            throw new ServiceException("Le mot de passe ne doit pas dépasser " + MDP_MAX + " caractères.");
        }
        boolean lettre = mdp.chars().anyMatch(Character::isLetter);
        boolean chiffre = mdp.chars().anyMatch(Character::isDigit);
        if (!lettre || !chiffre) {
            throw new ServiceException("Le mot de passe doit contenir au moins une lettre et un chiffre.");
        }
    }

    private String verifierTelephone(String telephone) {
        String t = optionnel(telephone, 20, "Le téléphone");
        if (t != null && !TELEPHONE.matcher(t).matches()) {
            throw new ServiceException("Le numéro de téléphone est invalide.");
        }
        return t;
    }

    private LocalDate verifierDateNaissance(LocalDate date) {
        if (date == null) {
            return null; // facultative
        }
        if (!date.isBefore(LocalDate.now()) || date.isBefore(LocalDate.of(1900, 1, 1))) {
            throw new ServiceException("La date de naissance est invalide.");
        }
        return date;
    }

    private static String obligatoire(String valeur, int max, String libelle) {
        if (estVide(valeur)) {
            throw new ServiceException(libelle + " est obligatoire.");
        }
        String v = valeur.trim();
        if (v.length() > max) {
            throw new ServiceException(libelle + " ne doit pas dépasser " + max + " caractères.");
        }
        return v;
    }

    private static String optionnel(String valeur, int max, String libelle) {
        return estVide(valeur) ? null : obligatoire(valeur, max, libelle);
    }

    private static boolean estVide(String s) {
        return s == null || s.isBlank();
    }

    private static String normaliser(String email) {
        return email.trim().toLowerCase();
    }

    // ---------------------------------------------------------------
    // BCrypt et accès aux données
    // ---------------------------------------------------------------

    private static String hacher(String motDePasse) {
        return BCrypt.hashpw(motDePasse, BCrypt.gensalt());
    }

    private static boolean motDePasseCorrect(String clair, String hache) {
        try {
            return hache != null && BCrypt.checkpw(clair, hache);
        } catch (IllegalArgumentException e) {
            return false; // valeur en base qui n'est pas un hash BCrypt valide
        }
    }

    private Utilisateur chargerOuErreur(Long id) {
        Utilisateur u = (id == null) ? null : dao.trouverParId(id);
        if (u == null) {
            throw new ServiceException("Utilisateur introuvable.");
        }
        return u;
    }

    /** Filet de sécurité : la base refuse aussi les doublons (email, matricule), même en cas de concurrence. */
    private void enregistrer(Utilisateur u) {
        try {
            dao.ajouter(u);
        } catch (RuntimeException e) {
            for (Throwable t = e; t != null; t = t.getCause()) {
                if (t instanceof ConstraintViolationException) {
                    throw new ServiceException("Un compte existe déjà avec cet email ou ce matricule.");
                }
            }
            throw e;
        }
    }
    
    public void changerRole(Long id, String nouveauRole) {

        if (nouveauRole == null) {
            throw new ServiceException("Veuillez sélectionner un rôle.");
        }

        String role = nouveauRole.trim().toUpperCase();

        if (!role.equals("CLIENT") && !role.equals("AGENT") && !role.equals("ADMIN")) {
            throw new ServiceException("Rôle invalide.");
        }

        Utilisateur utilisateur = chargerOuErreur(id);

        // Éviter de modifier inutilement le rôle
        if (utilisateur instanceof Administrateur && role.equals("ADMIN")
                || utilisateur instanceof Agent && role.equals("AGENT")
                || utilisateur instanceof Client && role.equals("CLIENT")) {
            throw new ServiceException("Cet utilisateur possède déjà ce rôle.");
        }

        // Empêcher la suppression du dernier administrateur actif
        if (utilisateur instanceof Administrateur
                && utilisateur.isActif()
                && !role.equals("ADMIN")
                && dao.compterActifsParType("ADMIN") <= 1) {

            throw new ServiceException(
                    "Impossible de retirer le rôle du dernier administrateur actif.");
        }

        if (!dao.modifierRole(id, role)) {
            throw new ServiceException("Impossible de modifier le rôle.");
        }
    }
}
