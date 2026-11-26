package com.assurance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.assurance.model.Statistiques;
import com.assurance.util.DatabaseConnection;

public class StatistiquesDAO {

    public Statistiques recupererStatistiques() {

        Statistiques stats = new Statistiques();

        try (Connection conn = DatabaseConnection.getConnection()) {

            // Indicateurs généraux
            stats.setNombreUtilisateurs(
                compter(conn, "SELECT COUNT(*) FROM utilisateurs"));

            stats.setNombreClients(
                compter(conn,
                    "SELECT COUNT(*) FROM utilisateurs WHERE type_utilisateur = 'CLIENT'"));

            stats.setNombreContrats(
                compter(conn, "SELECT COUNT(*) FROM contrats"));

            stats.setContratsActifs(
                compter(conn,
                    "SELECT COUNT(*) FROM contrats WHERE statut = 'ACTIF'"));

            stats.setNombreSinistres(
                compter(conn, "SELECT COUNT(*) FROM sinistres"));

            stats.setSinistresEnAttente(
                compter(conn, "SELECT COUNT(*) FROM sinistres WHERE statut IN ('DECLARE', 'EN_ANALYSE')"));

            stats.setNombrePaiements(
                compter(conn, "SELECT COUNT(*) FROM paiements"));

            stats.setEcheancesImpayees(
                compter(conn,
                    "SELECT COUNT(*) FROM echeances WHERE statut = 'IMPAYEE'"));

            // Montant total des paiements
            String sqlPaiements =
                "SELECT COALESCE(SUM(montant), 0) FROM paiements";

            try (PreparedStatement ps = conn.prepareStatement(sqlPaiements);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    stats.setMontantTotalPaiements(rs.getDouble(1));
                }
            }

            // Données des graphiques
            stats.setContratsParType(recupererContratsParType(conn));

            stats.setContratsParMois(recupererContratsParMois(conn));

            stats.setSinistresParStatut(recupererSinistresParStatut(conn));

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

    // Graphique 1 : contrats par type d'assurance
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

    // Graphique 2 : évolution des contrats sur les 6 derniers mois
    private Map<String, Integer> recupererContratsParMois(Connection conn)
            throws SQLException {

        Map<String, Integer> resultats = new LinkedHashMap<>();

        String sql =
            "SELECT DATE_FORMAT(date_creation, '%m/%Y') AS mois, " +
            "COUNT(*) AS total " +
            "FROM contrats " +
            "WHERE date_creation >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) " +
            "GROUP BY YEAR(date_creation), MONTH(date_creation), " +
            "DATE_FORMAT(date_creation, '%m/%Y') " +
            "ORDER BY YEAR(date_creation), MONTH(date_creation)";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultats.put(rs.getString("mois"), rs.getInt("total"));
            }
        }

        return resultats;
    }

    // Graphique 3 : répartition des sinistres par statut
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