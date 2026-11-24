package com.assurance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.assurance.model.Statistiques;
import com.assurance.util.DatabaseConnection;

public class StatistiquesDAO {

    public Statistiques recupererStatistiques() {

        Statistiques stats = new Statistiques();

        try (Connection conn = DatabaseConnection.getConnection()) {

            stats.setNombreUtilisateurs(
                compter(conn, "SELECT COUNT(*) FROM utilisateurs"));

            stats.setNombreContrats(
                compter(conn, "SELECT COUNT(*) FROM contrats"));

            stats.setContratsActifs(
                compter(conn, "SELECT COUNT(*) FROM contrats WHERE statut = 'ACTIF'"));

            stats.setNombreSinistres(
                compter(conn, "SELECT COUNT(*) FROM sinistres"));

            stats.setSinistresEnAttente(
                compter(conn, "SELECT COUNT(*) FROM sinistres WHERE statut IN ('DECLARE', 'EN_ANALYSE')"));

            stats.setNombrePaiements(
                compter(conn, "SELECT COUNT(*) FROM paiements"));

            stats.setEcheancesImpayees(
                compter(conn, "SELECT COUNT(*) FROM echeances WHERE statut = 'IMPAYEE'"));

            String sql = "SELECT COALESCE(SUM(montant), 0) FROM paiements";

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    stats.setMontantTotalPaiements(rs.getDouble(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }

    private int compter(Connection conn, String sql) throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }
}