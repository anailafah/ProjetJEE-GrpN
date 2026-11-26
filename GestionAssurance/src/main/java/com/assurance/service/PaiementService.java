package com.assurance.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import com.assurance.dao.PaiementDAO;
import com.assurance.model.Echeance;
import com.assurance.model.Paiement;

public class PaiementService {

    // Statuts d'un paiement (EN_ATTENTE est la valeur par défaut de la base)
    public static final String STATUT_EN_ATTENTE = "EN_ATTENTE";
    public static final String STATUT_EFFECTUE = "EFFECTUE";

    public static final List<String> MODES_PAIEMENT = List.of(
            "CARTE", "VIREMENT", "PRELEVEMENT", "CHEQUE", "ESPECES");

    private PaiementDAO paiementDAO = new PaiementDAO();
    private EcheanceService echeanceService = new EcheanceService();

    // ------------------------------------------------------------------
    // Enregistrement
    // ------------------------------------------------------------------

    // Paiement reçu et encaissé : il compte tout de suite dans le montant payé.
    public Paiement enregistrerPaiement(Long echeanceId, BigDecimal montant,
            String modePaiement, String reference) {

        return creerPaiement(echeanceId, montant, modePaiement, reference,
                STATUT_EFFECTUE);
    }

    // Paiement annoncé mais pas encore encaissé (virement, chèque...) :
    // il apparaît dans "paiements en attente" et n'affecte pas l'échéance.
    public Paiement declarerPaiementEnAttente(Long echeanceId, BigDecimal montant,
            String modePaiement, String reference) {

        return creerPaiement(echeanceId, montant, modePaiement, reference,
                STATUT_EN_ATTENTE);
    }

    // Confirme l'encaissement d'un paiement en attente.
    public Paiement validerPaiement(Long paiementId) {

        Paiement paiement = trouverParId(paiementId);

        if (paiement == null) {
            throw new IllegalArgumentException("Paiement introuvable.");
        }

        if (!STATUT_EN_ATTENTE.equals(paiement.getStatut())) {
            throw new IllegalArgumentException(
                    "Seul un paiement en attente peut être validé.");
        }

        Echeance echeance = paiement.getEcheance();
        BigDecimal reste = calculerReste(echeance);

        if (paiement.getMontant().compareTo(reste) > 0) {
            throw new IllegalArgumentException(
                    "Le montant dépasse le reste à payer (" + reste + ").");
        }

        paiement.setStatut(STATUT_EFFECTUE);
        paiement.setDatePaiement(LocalDateTime.now());

        paiementDAO.enregistrer(paiement, statutEcheanceApres(paiement, reste));

        return paiement;
    }

    private Paiement creerPaiement(Long echeanceId, BigDecimal montant,
            String modePaiement, String reference, String statut) {

        Echeance echeance = echeanceService.trouverParId(echeanceId);

        if (echeance == null) {
            throw new IllegalArgumentException("Échéance introuvable.");
        }

        if (EcheanceService.STATUT_PAYEE.equals(echeance.getStatut())) {
            throw new IllegalArgumentException("Cette échéance est déjà payée.");
        }

        verifierMontantEtMode(montant, modePaiement);

        BigDecimal montantArrondi = montant.setScale(2, RoundingMode.HALF_UP);
        BigDecimal reste = calculerReste(echeance);

        if (montantArrondi.compareTo(reste) > 0) {
            throw new IllegalArgumentException(
                    "Le montant dépasse le reste à payer (" + reste + ").");
        }

        Paiement paiement = new Paiement();
        paiement.setEcheance(echeance);
        paiement.setMontant(montantArrondi);
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setModePaiement(modePaiement.trim().toUpperCase());
        paiement.setReferencePaiement(preparerReference(reference));
        paiement.setStatut(statut);

        // Paiement + passage de l'échéance à PAYEE dans une seule transaction
        paiementDAO.enregistrer(paiement, statutEcheanceApres(paiement, reste));

        return paiement;
    }

    // L'échéance passe à PAYEE uniquement si ce paiement est encaissé
    // et solde exactement ce qu'il restait à payer. Sinon : null = pas de changement
    // (un paiement partiel laisse l'échéance EN_ATTENTE ou EN_RETARD).
    private String statutEcheanceApres(Paiement paiement, BigDecimal reste) {

        boolean encaisse = STATUT_EFFECTUE.equals(paiement.getStatut());
        boolean solde = paiement.getMontant().compareTo(reste) == 0;

        return (encaisse && solde) ? EcheanceService.STATUT_PAYEE : null;
    }

    // ------------------------------------------------------------------
    // Montants
    // ------------------------------------------------------------------

    // Montant de l'échéance moins les paiements déjà encaissés
    public BigDecimal resteAPayer(Long echeanceId) {

        Echeance echeance = echeanceService.trouverParId(echeanceId);

        if (echeance == null) {
            throw new IllegalArgumentException("Échéance introuvable.");
        }

        return calculerReste(echeance);
    }

    // Variante sans rechargement de l'échéance (pour les listes d'échéances)
    public BigDecimal resteAPayer(Echeance echeance) {
        return calculerReste(echeance);
    }

    private BigDecimal calculerReste(Echeance echeance) {

        BigDecimal paye = paiementDAO.sommeParEcheanceEtStatut(
                echeance.getId(), STATUT_EFFECTUE);

        return echeance.getMontant().subtract(paye);
    }

    // ------------------------------------------------------------------
    // Consultation / historique
    // ------------------------------------------------------------------

    public Paiement trouverParId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }

        return paiementDAO.trouverParId(id);
    }

    // Historique complet (agent / administrateur)
    public List<Paiement> listerHistorique() {
        return paiementDAO.lister();
    }

    // Paiements effectués
    public List<Paiement> listerEffectues() {
        return paiementDAO.listerParStatut(STATUT_EFFECTUE);
    }

    // Paiements en attente
    public List<Paiement> listerEnAttente() {
        return paiementDAO.listerParStatut(STATUT_EN_ATTENTE);
    }

    public List<Paiement> listerParEcheance(Long echeanceId) {

        if (echeanceId == null || echeanceId <= 0) {
            throw new IllegalArgumentException("Identifiant d'échéance invalide.");
        }

        return paiementDAO.listerParEcheance(echeanceId);
    }

    public List<Paiement> historiqueParContrat(Long contratId) {

        if (contratId == null || contratId <= 0) {
            throw new IllegalArgumentException("Identifiant de contrat invalide.");
        }

        return paiementDAO.listerParContrat(contratId);
    }

    // Espace client : un client ne voit que ses paiements
    public List<Paiement> historiqueParClient(Long clientId) {

        if (clientId == null || clientId <= 0) {
            throw new IllegalArgumentException("Identifiant de client invalide.");
        }

        return paiementDAO.listerParClient(clientId);
    }

    // ------------------------------------------------------------------
    // Vérifications
    // ------------------------------------------------------------------

    private void verifierMontantEtMode(BigDecimal montant, String modePaiement) {

        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à zéro.");
        }

        if (modePaiement == null
                || !MODES_PAIEMENT.contains(modePaiement.trim().toUpperCase())) {
            throw new IllegalArgumentException(
                    "Mode de paiement invalide (" + String.join(", ", MODES_PAIEMENT) + ").");
        }
    }

    // Référence saisie (100 caractères max, comme la colonne) ou générée : PAY-20261118-A1B2C3D4
    private String preparerReference(String reference) {

        if (reference == null || reference.trim().isEmpty()) {
            return "PAY-"
                    + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                    + "-"
                    + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }

        String propre = reference.trim();

        if (propre.length() > 100) {
            throw new IllegalArgumentException(
                    "La référence ne doit pas dépasser 100 caractères.");
        }

        return propre;
    }
}
