package com.assurance.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import com.assurance.model.Statistiques;
import com.assurance.util.DatabaseConnection;

public class StatistiquesDAO {

    /** Nombre de mois affichés dans les graphiques d'évolution. */
    private static final int NB_MOIS_EVOLUTION = 6;

    /** Une période est « récente » si elle date de moins de N jours (nouveaux clients). */
    private static final int JOURS_NOUVEAUX_CLIENTS = 30;

    private static final DateTimeFormatter FORMAT_MOIS = DateTimeFormatter.ofPattern("MM/yyyy");

    public Statistiques recupererStatistiques() {

        Statistiques stats = new Statistiques();

        try (Connection conn = DatabaseConnection.getConnection()) {

            // Indicateurs généraux
            stats.setNombreUtilisateurs(
                compter(conn, "SELECT COUNT(*) FROM utilisateurs"));

            // Statistiques clients
            stats.setNombreClients(
                compter(conn,
                    "SELECT COUNT(*) FROM utilisateurs WHERE type_utilisateur = 'CLIENT'"));

            stats.setNouveauxClients(
                compter(conn,
                    "SELECT COUNT(*) FROM utilisateurs "
                    + "WHERE type_utilisateur = 'CLIENT' "
                    + "AND date_creation >= DATE_SUB(CURDATE(), INTERVAL "
                    + JOURS_NOUVEAUX_CLIENTS + " DAY)"));

            // Statistiques contrats
            stats.setNombreContrats(
                compter(conn, "SELECT COUNT(*) FROM contrats"));

            stats.setContratsActifs(
                compter(conn, "SELECT COUNT(*) FROM contrats WHERE statut = 'ACTIF'"));

            stats.setContratsExpires(
                compter(conn, "SELECT COUNT(*) FROM contrats WHERE statut = 'EXPIRE'"));

            stats.setContratsResilies(
                compter(conn, "SELECT COUNT(*) FROM contrats WHERE statut = 'RESILIE'"));

            // Statistiques sinistres
            stats.setNombreSinistres(
                compter(conn, "SELECT COUNT(*) FROM sinistres"));

            stats.setSinistresEnAttente(
                compter(conn, "SELECT COUNT(*) FROM sinistres WHERE statut IN ('DECLARE', 'EN_ANALYSE')"));

            // Paiements et échéances
            // (échéances impayées : date passée et toujours dues ; les ANNULEE ne comptent pas)
            stats.setNombrePaiements(
                compter(conn, "SELECT COUNT(*) FROM paiements"));

            stats.setEcheancesImpayees(
                compter(conn,
                        "SELECT COUNT(*) FROM echeances WHERE statut IN ('EN_ATTENTE', 'EN_RETARD') AND date_echeance < CURDATE()"));

            // Statistiques financières
            stats.setMontantTotalPaiements(
                somme(conn, "SELECT COALESCE(SUM(montant), 0) FROM paiements"));

            stats.setChiffreAffaires(
                somme(conn,
                    "SELECT COALESCE(SUM(montant), 0) FROM paiements WHERE statut = 'EFFECTUE'"));

            stats.setMontantPrimes(
                somme(conn,
                    "SELECT COALESCE(SUM(montant), 0) FROM contrats WHERE statut = 'ACTIF'"));

            stats.setMontantIndemnisations(
                somme(conn,
                    "SELECT COALESCE(SUM(montant_indemnisation), 0) FROM sinistres "
                    + "WHERE statut = 'INDEMNISE'"));

            // Données des graphiques
            stats.setContratsParType(recupererContratsParType(conn));
            stats.setClientsParType(recupererClientsParType(conn));
            stats.setContratsParMois(recupererContratsParMois(conn));
            stats.setSinistresParStatut(recupererSinistresParStatut(conn));
            stats.setRevenusParMois(recupererRevenusParMois(conn));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }

    // Compter les enregistrements
    private int compter(Connection conn, String sql) throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

    // Calculer une somme (montants)
    private double somme(Connection conn, String sql) throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }
        }

        return 0;
    }

    // Graphique : contrats par type d'assurance
    private Map<String, Integer> recupererContratsParType(Connection conn)
            throws SQLException {

        Map<String, Integer> resultats = new LinkedHashMap<>();

        String sql =
            "SELECT ta.nom, COUNT(c.id) AS total " +
            "FROM types_assurance ta " +
            "LEFT JOIN contrats c ON c.type_assurance_id = ta.id " +
            "GROUP BY ta.id, ta.nom " +
            "ORDER BY ta.nom";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultats.put(rs.getString("nom"), rs.getInt("total"));
            }
        }

        return resultats;
    }

    // Graphique : répartition des clients par type d'assurance
    // (un client ayant plusieurs contrats du même type n'est compté qu'une fois)
    private Map<String, Integer> recupererClientsParType(Connection conn)
            throws SQLException {

        Map<String, Integer> resultats = new LinkedHashMap<>();

        String sql =
            "SELECT ta.nom, COUNT(DISTINCT c.client_id) AS total " +
            "FROM types_assurance ta " +
            "LEFT JOIN contrats c ON c.type_assurance_id = ta.id " +
            "GROUP BY ta.id, ta.nom " +
            "ORDER BY ta.nom";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultats.put(rs.getString("nom"), rs.getInt("total"));
            }
        }

        return resultats;
    }

    // Graphique : évolution des contrats créés sur les derniers mois
    private Map<String, Integer> recupererContratsParMois(Connection conn)
            throws SQLException {

        String sql =
            "SELECT YEAR(date_creation) AS annee, MONTH(date_creation) AS mois, " +
            "COUNT(*) AS total " +
            "FROM contrats " +
            "WHERE date_creation >= ? " +
            "GROUP BY YEAR(date_creation), MONTH(date_creation)";

        Map<String, Double> valeurs = parMois(conn, sql);

        Map<String, Integer> resultats = new LinkedHashMap<>();
        valeurs.forEach((mois, total) -> resultats.put(mois, total.intValue()));
        return resultats;
    }

    // Graphique : évolution des revenus (paiements effectués) sur les derniers mois
    private Map<String, Double> recupererRevenusParMois(Connection conn)
            throws SQLException {

        String sql =
            "SELECT YEAR(date_paiement) AS annee, MONTH(date_paiement) AS mois, " +
            "COALESCE(SUM(montant), 0) AS total " +
            "FROM paiements " +
            "WHERE statut = 'EFFECTUE' AND date_paiement >= ? " +
            "GROUP BY YEAR(date_paiement), MONTH(date_paiement)";

        return parMois(conn, sql);
    }

    /**
     * Exécute une requête groupée par (annee, mois) et renvoie une valeur
     * pour chacun des NB_MOIS_EVOLUTION derniers mois, y compris les mois
     * sans données (valeur 0), pour que les courbes n'aient pas de trou.
     * La requête doit avoir un seul paramètre : la date de début.
     */
    private Map<String, Double> parMois(Connection conn, String sql)
            throws SQLException {

        YearMonth moisCourant = YearMonth.now();
        YearMonth premierMois = moisCourant.minusMonths(NB_MOIS_EVOLUTION - 1);

        Map<String, Double> resultats = new LinkedHashMap<>();
        for (YearMonth m = premierMois; !m.isAfter(moisCourant); m = m.plusMonths(1)) {
            resultats.put(m.format(FORMAT_MOIS), 0.0);
        }

        LocalDate debut = premierMois.atDay(1);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(debut));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    YearMonth m = YearMonth.of(rs.getInt("annee"), rs.getInt("mois"));
                    resultats.put(m.format(FORMAT_MOIS), rs.getDouble("total"));
                }
            }
        }

        return resultats;
    }

    // Graphique : répartition des sinistres par statut
    private Map<String, Integer> recupererSinistresParStatut(Connection conn)
            throws SQLException {

        Map<String, Integer> resultats = new LinkedHashMap<>();

        String sql =
            "SELECT statut, COUNT(*) AS total " +
            "FROM sinistres " +
            "GROUP BY statut " +
            "ORDER BY statut";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultats.put(rs.getString("statut"), rs.getInt("total"));
            }
        }

        return resultats;
    }
}
