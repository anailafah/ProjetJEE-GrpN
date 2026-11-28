package com.assurance.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.assurance.dao.ContratDAO;
import com.assurance.dao.SinistreDAO;
import com.assurance.model.CommentaireSinistre;
import com.assurance.model.Contrat;
import com.assurance.model.DocumentSinistre;
import com.assurance.model.Garantie;
import com.assurance.model.Sinistre;
import com.assurance.model.StatutSinistre;

public class SinistreService {

    private SinistreDAO sinistreDAO = new SinistreDAO();
    private ContratDAO contratDAO = new ContratDAO();

    /**
     * Changements de statut autorisés.
     * DECLARE -> EN_ANALYSE -> ACCEPTE ou REFUSE, puis ACCEPTE -> INDEMNISE.
     * Le passage à INDEMNISE se fait uniquement par indemniser(), qui exige un montant.
     */
    private static final Map<StatutSinistre, Set<StatutSinistre>> TRANSITIONS = Map.of(
        StatutSinistre.DECLARE,    EnumSet.of(StatutSinistre.EN_ANALYSE),
        StatutSinistre.EN_ANALYSE, EnumSet.of(StatutSinistre.ACCEPTE, StatutSinistre.REFUSE),
        StatutSinistre.ACCEPTE,    EnumSet.of(StatutSinistre.INDEMNISE),
        StatutSinistre.REFUSE,     EnumSet.noneOf(StatutSinistre.class),
        StatutSinistre.INDEMNISE,  EnumSet.noneOf(StatutSinistre.class)
    );

    // ------------------------------------------------------------------
    // Côté client
    // ------------------------------------------------------------------

    /**
     * Déclaration d'un sinistre par un client.
     * Le sinistre reçu doit avoir : typeSinistre, dateSinistre, description, montantDemande.
     * Retourne le sinistre enregistré (avec son numéro généré).
     */
    public Sinistre declarer(Sinistre sinistre, Long contratId, Long clientId) {

        if (sinistre == null) {
            throw new IllegalArgumentException("Sinistre obligatoire.");
        }

        if (clientId == null) {
            throw new IllegalArgumentException("Client non identifié.");
        }

        if (contratId == null) {
            throw new IllegalArgumentException("Veuillez choisir un contrat.");
        }

        Contrat contrat = contratDAO.trouverParId(contratId);

        if (contrat == null) {
            throw new IllegalArgumentException("Contrat introuvable.");
        }

        // Un client ne peut déclarer que sur ses propres contrats
        if (contrat.getClient() == null || !clientId.equals(contrat.getClient().getId())) {
            throw new IllegalArgumentException("Ce contrat ne vous appartient pas.");
        }

        if (!"ACTIF".equalsIgnoreCase(contrat.getStatut())) {
            throw new IllegalArgumentException(
                "Vous ne pouvez déclarer un sinistre que sur un contrat actif.");
        }

        verifierDeclaration(sinistre, contrat);

        sinistre.setContrat(contrat);
        sinistre.setStatut(StatutSinistre.DECLARE);
        sinistre.setMontantIndemnisation(null);
        sinistre.setDateDeclaration(LocalDateTime.now());
        sinistre.setNumeroSinistre(genererNumero());

        sinistreDAO.ajouter(sinistre);

        return sinistre;
    }

    public List<Sinistre> listerParClient(Long clientId) {

        if (clientId == null) {
            throw new IllegalArgumentException("Client non identifié.");
        }

        return sinistreDAO.listerParClient(clientId);
    }

    /**
     * Retourne le sinistre seulement s'il appartient au client.
     * A utiliser dans les pages client pour qu'un client ne puisse pas
     * voir le sinistre d'un autre en changeant l'id dans l'URL.
     */
    public Sinistre trouverPourClient(Long sinistreId, Long clientId) {

        Sinistre sinistre = trouverParId(sinistreId);

        if (sinistre == null
                || sinistre.getContrat() == null
                || sinistre.getContrat().getClient() == null
                || !sinistre.getContrat().getClient().getId().equals(clientId)) {
            throw new IllegalArgumentException("Sinistre introuvable.");
        }

        return sinistre;
    }

    // ------------------------------------------------------------------
    // Côté agent / admin
    // ------------------------------------------------------------------

    public List<Sinistre> lister() {
        return sinistreDAO.lister();
    }

    public List<Sinistre> listerParStatut(StatutSinistre statut) {

        if (statut == null) {
            return sinistreDAO.lister();
        }

        return sinistreDAO.listerParStatut(statut);
    }

    public Sinistre trouverParId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }

        return sinistreDAO.trouverParId(id);
    }

    /**
     * Changement de statut par l'agent (sauf INDEMNISE : utiliser indemniser()).
     */
    public void changerStatut(Long sinistreId, StatutSinistre nouveauStatut) {

        if (nouveauStatut == null) {
            throw new IllegalArgumentException("Statut obligatoire.");
        }

        if (nouveauStatut == StatutSinistre.INDEMNISE) {
            throw new IllegalArgumentException(
                "Pour indemniser un sinistre, saisissez le montant d'indemnisation.");
        }

        Sinistre sinistre = trouverExistant(sinistreId);

        verifierTransition(sinistre.getStatut(), nouveauStatut);

        // Accepter un sinistre sur un contrat sans garantie le bloquerait :
        // il ne pourrait plus être ni indemnisé ni refusé.
        if (nouveauStatut == StatutSinistre.ACCEPTE && garantiesDuContrat(sinistre).isEmpty()) {
            throw new IllegalArgumentException(
                "Le contrat de ce client ne comporte aucune garantie : "
                + "le sinistre n'est pas couvert, il doit être refusé.");
        }

        sinistre.setStatut(nouveauStatut);

        sinistreDAO.modifier(sinistre);
    }

    /**
     * Enregistre l'indemnisation et passe le sinistre à INDEMNISE. Le sinistre doit être ACCEPTE.
     *
     * Règles :
     *  - la garantie choisie doit faire partie du contrat du client ;
     *  - montant ≤ plafond de la garantie (montantCouverture), s'il est renseigné ;
     *  - montant ≤ montant demandé par le client ;
     *  - montant < montant demandé : motif obligatoire (vétusté, expertise, plafond...).
     * Un commentaire récapitulatif est ajouté au dossier, visible par le client.
     */
    public void indemniser(Long sinistreId, Long garantieId, BigDecimal montant,
                           String motif, Long agentId) {

        if (agentId == null) {
            throw new IllegalArgumentException("Utilisateur non identifié.");
        }

        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "Le montant d'indemnisation doit être supérieur à zéro.");
        }

        if (montant.stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException(
                "Le montant d'indemnisation doit avoir au plus 2 décimales.");
        }

        String motifNettoye = motif == null ? "" : motif.trim();

        if (motifNettoye.length() > 1500) {
            throw new IllegalArgumentException("Le motif ne doit pas dépasser 1500 caractères.");
        }

        Sinistre sinistre = trouverExistant(sinistreId);

        verifierTransition(sinistre.getStatut(), StatutSinistre.INDEMNISE);

        // 1. La garantie doit faire partie du contrat du client
        if (garantieId == null) {
            throw new IllegalArgumentException(
                "Choisissez la garantie du contrat qui couvre ce sinistre.");
        }

        Garantie garantie = garantiesDuContrat(sinistre).stream()
                .filter(g -> garantieId.equals(g.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                    "Cette garantie ne fait pas partie du contrat du client."));

        // 2. Jamais plus que le plafond de la garantie
        BigDecimal plafond = garantie.getMontantCouverture();

        if (plafond != null && montant.compareTo(plafond) > 0) {
            throw new IllegalArgumentException(
                "Le montant dépasse le plafond de la garantie « " + garantie.getNom()
                + " » (" + plafond + " €).");
        }

        // 3. Jamais plus que ce que le client a demandé
        BigDecimal demande = sinistre.getMontantDemande();

        if (demande != null && montant.compareTo(demande) > 0) {
            throw new IllegalArgumentException(
                "Le montant dépasse le montant demandé par le client (" + demande + " €).");
        }

        // 4. Un remboursement partiel doit être justifié
        boolean partiel = demande != null && montant.compareTo(demande) < 0;

        if (partiel && motifNettoye.isEmpty()) {
            throw new IllegalArgumentException(
                "Le montant est inférieur à la demande du client (" + demande
                + " €) : indiquez le motif (plafond, vétusté, expertise...).");
        }

        sinistre.setGarantie(garantie);
        sinistre.setMontantIndemnisation(montant);
        sinistre.setStatut(StatutSinistre.INDEMNISE);

        sinistreDAO.modifier(sinistre);

        // Trace visible par le client dans le suivi de son dossier
        StringBuilder trace = new StringBuilder()
                .append("[Indemnisé] ").append(montant).append(" €")
                .append(" au titre de la garantie « ").append(garantie.getNom()).append(" »");

        if (partiel) {
            trace.append(" (montant demandé : ").append(demande).append(" €)");
        }

        trace.append('.');

        if (!motifNettoye.isEmpty()) {
            trace.append(" Motif : ").append(motifNettoye);
        }

        sinistreDAO.ajouterCommentaire(sinistreId, agentId, trace.toString());
    }

    /**
     * Garanties du contrat concerné par le sinistre, avec leur plafond.
     * Une garantie retirée du catalogue (désactivée) reste valable pour les contrats
     * qui l'ont déjà souscrite : on ne filtre donc pas sur "actif".
     */
    public List<Garantie> garantiesDuContrat(Sinistre sinistre) {

        if (sinistre == null || sinistre.getContrat() == null) {
            return List.of();
        }

        Contrat contrat = contratDAO.trouverParId(sinistre.getContrat().getId());

        if (contrat == null || contrat.getGaranties() == null) {
            return List.of();
        }

        return contrat.getGaranties();
    }

    /**
     * Montant proposé par défaut pour une garantie : le plus petit entre la demande et le plafond.
     */
    public BigDecimal montantPropose(Sinistre sinistre, Garantie garantie) {

        BigDecimal demande = sinistre.getMontantDemande();
        BigDecimal plafond = garantie.getMontantCouverture();

        if (demande == null) {
            return plafond;
        }

        if (plafond == null) {
            return demande;
        }

        return demande.min(plafond);
    }

    /**
     * Statuts que l'agent peut choisir depuis le statut actuel
     * (pratique pour remplir la liste déroulante dans la JSP).
     */
    public Set<StatutSinistre> statutsPossibles(StatutSinistre statutActuel) {

        if (statutActuel == null) {
            return EnumSet.noneOf(StatutSinistre.class);
        }

        return TRANSITIONS.get(statutActuel);
    }

    // ------------------------------------------------------------------
    // Commentaires et pièces justificatives
    // ------------------------------------------------------------------

    public void ajouterCommentaire(Long sinistreId, Long utilisateurId, String texte) {

        if (utilisateurId == null) {
            throw new IllegalArgumentException("Utilisateur non identifié.");
        }

        if (texte == null || texte.trim().isEmpty()) {
            throw new IllegalArgumentException("Le commentaire ne peut pas être vide.");
        }

        if (texte.trim().length() > 2000) {
            throw new IllegalArgumentException(
                "Le commentaire ne doit pas dépasser 2000 caractères.");
        }

        trouverExistant(sinistreId);

        sinistreDAO.ajouterCommentaire(sinistreId, utilisateurId, texte.trim());
    }

    public List<CommentaireSinistre> listerCommentaires(Long sinistreId) {
        return sinistreDAO.listerCommentaires(sinistreId);
    }

    /**
     * Enregistre en base une pièce justificative déjà copiée sur le disque
     * (la copie du fichier sera faite par la servlet d'upload).
     */
    public void ajouterDocument(Long sinistreId, String nomFichier, String cheminFichier) {

        if (nomFichier == null || nomFichier.trim().isEmpty()
                || cheminFichier == null || cheminFichier.trim().isEmpty()) {
            throw new IllegalArgumentException("Fichier invalide.");
        }

        trouverExistant(sinistreId);

        sinistreDAO.ajouterDocument(sinistreId, nomFichier.trim(), cheminFichier.trim());
    }

    public List<DocumentSinistre> listerDocuments(Long sinistreId) {
        return sinistreDAO.listerDocuments(sinistreId);
    }

    public DocumentSinistre trouverDocumentParId(Long documentId) {

        if (documentId == null || documentId <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }

        return sinistreDAO.trouverDocumentParId(documentId);
    }

    // ------------------------------------------------------------------
    // Méthodes internes
    // ------------------------------------------------------------------

    private Sinistre trouverExistant(Long sinistreId) {

        Sinistre sinistre = trouverParId(sinistreId);

        if (sinistre == null) {
            throw new IllegalArgumentException("Sinistre introuvable.");
        }

        return sinistre;
    }

    private void verifierTransition(StatutSinistre actuel, StatutSinistre nouveau) {

        Set<StatutSinistre> autorises = statutsPossibles(actuel);

        if (!autorises.contains(nouveau)) {
            throw new IllegalArgumentException(
                "Impossible de passer de « " + libelle(actuel)
                + " » à « " + libelle(nouveau) + " ».");
        }
    }

    private String libelle(StatutSinistre statut) {
        return statut == null ? "inconnu" : statut.getLibelle();
    }

    private void verifierDeclaration(Sinistre sinistre, Contrat contrat) {

        if (sinistre.getTypeSinistre() == null) {
            throw new IllegalArgumentException("Le type de sinistre est obligatoire.");
        }

        LocalDate date = sinistre.getDateSinistre();

        if (date == null) {
            throw new IllegalArgumentException("La date du sinistre est obligatoire.");
        }

        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                "La date du sinistre ne peut pas être dans le futur.");
        }

        if (contrat.getDateDebut() != null && date.isBefore(contrat.getDateDebut())) {
            throw new IllegalArgumentException(
                "La date du sinistre est antérieure au début du contrat.");
        }

        if (contrat.getDateFin() != null && date.isAfter(contrat.getDateFin())) {
            throw new IllegalArgumentException(
                "La date du sinistre est postérieure à la fin du contrat.");
        }

        String description = sinistre.getDescription();

        if (description == null || description.trim().length() < 10) {
            throw new IllegalArgumentException(
                "Veuillez décrire le sinistre (10 caractères minimum).");
        }

        sinistre.setDescription(description.trim());

        BigDecimal montant = sinistre.getMontantDemande();

        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "Le montant estimé doit être supérieur à zéro.");
        }
    }

    /**
     * Génère un numéro du type SIN-2026-0001, en repartant de 1 chaque année.
     */
    private String genererNumero() {

        String prefixe = "SIN-" + LocalDate.now().getYear() + "-";

        String dernier = sinistreDAO.dernierNumero(prefixe);

        int suivant = 1;

        if (dernier != null) {
            try {
                suivant = Integer.parseInt(dernier.substring(prefixe.length())) + 1;
            } catch (NumberFormatException e) {
                suivant = 1;
            }
        }

        return prefixe + String.format("%04d", suivant);
    }
}
