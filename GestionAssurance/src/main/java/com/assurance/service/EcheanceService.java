package com.assurance.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.assurance.dao.EcheanceDAO;
import com.assurance.model.Contrat;
import com.assurance.model.Echeance;

public class EcheanceService {

    // Statuts d'une échéance (EN_ATTENTE est la valeur par défaut de la base)
    public static final String STATUT_EN_ATTENTE = "EN_ATTENTE";
    public static final String STATUT_PAYEE = "PAYEE";
    public static final String STATUT_EN_RETARD = "EN_RETARD";

    // Périodicité = nombre de mois entre deux échéances
    public static final int PERIODICITE_MENSUELLE = 1;
    public static final int PERIODICITE_TRIMESTRIELLE = 3;
    public static final int PERIODICITE_SEMESTRIELLE = 6;
    public static final int PERIODICITE_ANNUELLE = 12;

    private static final List<Integer> PERIODICITES_VALIDES = List.of(
            PERIODICITE_MENSUELLE,
            PERIODICITE_TRIMESTRIELLE,
            PERIODICITE_SEMESTRIELLE,
            PERIODICITE_ANNUELLE);

    // Durée utilisée quand le contrat n'a pas de date de fin
    private static final int DUREE_PAR_DEFAUT_MOIS = 12;

    private EcheanceDAO echeanceDAO = new EcheanceDAO();

    // ------------------------------------------------------------------
    // Génération de l'échéancier d'un contrat
    // ------------------------------------------------------------------

    public List<Echeance> genererEcheances(Contrat contrat) {
        return genererEcheances(contrat, PERIODICITE_MENSUELLE);
    }

    public List<Echeance> genererEcheances(Contrat contrat, int periodiciteMois) {

        if (contrat == null || contrat.getId() == null) {
            throw new IllegalArgumentException("Contrat invalide.");
        }

        if (echeanceDAO.compterParContrat(contrat.getId()) > 0) {
            throw new IllegalArgumentException(
                    "Des échéances existent déjà pour ce contrat.");
        }

        List<Echeance> echeances = calculerEcheances(contrat, periodiciteMois);

        echeanceDAO.ajouterTous(echeances);

        return echeances;
    }

    // Calcul pur (sans accès à la base) : le montant du contrat est la prime totale
    // sur sa durée, répartie en parts égales. La dernière échéance absorbe
    // l'écart d'arrondi pour que la somme des échéances soit exactement égale
    // à la prime.
    public static List<Echeance> calculerEcheances(Contrat contrat, int periodiciteMois) {

        if (!PERIODICITES_VALIDES.contains(periodiciteMois)) {
            throw new IllegalArgumentException(
                    "Périodicité invalide (1, 3, 6 ou 12 mois).");
        }

        if (contrat.getDateDebut() == null) {
            throw new IllegalArgumentException("Le contrat n'a pas de date de début.");
        }

        if (contrat.getMontant() == null
                || contrat.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Le montant du contrat doit être supérieur à zéro.");
        }

        LocalDate debut = contrat.getDateDebut();
        LocalDate fin = contrat.getDateFin() != null
                ? contrat.getDateFin()
                : debut.plusMonths(DUREE_PAR_DEFAUT_MOIS).minusDays(1);

        if (fin.isBefore(debut)) {
            throw new IllegalArgumentException(
                    "La date de fin précède la date de début.");
        }

        // fin + 1 jour : un contrat du 01/01 au 31/12 compte bien 12 mois
        long dureeMois = Math.max(1, ChronoUnit.MONTHS.between(debut, fin.plusDays(1)));

        int nombre = (int) ((dureeMois + periodiciteMois - 1) / periodiciteMois);

        BigDecimal total = contrat.getMontant().setScale(2, RoundingMode.HALF_UP);
        BigDecimal part = total.divide(BigDecimal.valueOf(nombre), 2, RoundingMode.HALF_UP);

        if (part.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Le montant du contrat est trop faible pour être réparti.");
        }

        List<Echeance> echeances = new ArrayList<>();
        BigDecimal cumule = BigDecimal.ZERO;

        for (int i = 0; i < nombre; i++) {
            BigDecimal montant = (i == nombre - 1) ? total.subtract(cumule) : part;
            cumule = cumule.add(montant);

            Echeance echeance = new Echeance();
            echeance.setContrat(contrat);
            echeance.setDateEcheance(debut.plusMonths((long) i * periodiciteMois));
            echeance.setMontant(montant);
            echeance.setStatut(STATUT_EN_ATTENTE);

            echeances.add(echeance);
        }

        return echeances;
    }

    // ------------------------------------------------------------------
    // Consultation
    // ------------------------------------------------------------------

    public Echeance trouverParId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }

        return echeanceDAO.trouverParId(id);
    }

    public List<Echeance> listerParContrat(Long contratId) {

        if (contratId == null || contratId <= 0) {
            throw new IllegalArgumentException("Identifiant de contrat invalide.");
        }

        return echeanceDAO.listerParContrat(contratId);
    }

    // Espace client : échéances de tous ses contrats
    public List<Echeance> listerParClient(Long clientId) {

        if (clientId == null || clientId <= 0) {
            throw new IllegalArgumentException("Identifiant de client invalide.");
        }

        return echeanceDAO.listerParClient(clientId);
    }

    public List<Echeance> listerEnAttente() {
        return echeanceDAO.listerParStatut(STATUT_EN_ATTENTE);
    }

    public List<Echeance> listerPayees() {
        return echeanceDAO.listerParStatut(STATUT_PAYEE);
    }

    // ------------------------------------------------------------------
    // Retards
    // ------------------------------------------------------------------

    // Échéances impayées dont la date est dépassée (en attente OU déjà en retard)
    public List<Echeance> detecterEcheancesImpayees() {
        return echeanceDAO.listerImpayeesAvant(LocalDate.now(), STATUT_PAYEE);
    }

    // Passe en EN_RETARD toutes les échéances en attente dont la date est dépassée.
    // Retourne le nombre d'échéances modifiées. À appeler à l'ouverture de la page
    // des échéances / du tableau de bord, ou par une tâche planifiée.
    public int gererRetards() {
        return echeanceDAO.changerStatutAvant(
                LocalDate.now(), STATUT_EN_ATTENTE, STATUT_EN_RETARD);
    }
}
