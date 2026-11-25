package com.assurance.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

public class CreerCompteAgent {

    public static void main(String[] args) {

        String nom = "Dupont";
        String prenom = "Jean";
        String email = "agent@gestionassurance.com";
        String telephone = "0600000000";
        String motDePasse = "Agent12345";

        try (Connection conn = DatabaseConnection.getConnection()) {

            String verification = "SELECT id FROM utilisateurs WHERE email = ?";

            try (PreparedStatement ps = conn.prepareStatement(verification)) {
                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Ce compte existe déjà !");
                        return;
                    }
                }
            }

            String motDePasseHash = BCrypt.hashpw(
                motDePasse,
                BCrypt.gensalt()
            );

            String sql = "INSERT INTO utilisateurs "
                + "(type_utilisateur, nom, prenom, email, mot_de_passe, telephone, date_creation, actif) "
                + "VALUES ('AGENT', ?, ?, ?, ?, ?, NOW(), 1)";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, nom);
                ps.setString(2, prenom);
                ps.setString(3, email);
                ps.setString(4, motDePasseHash);
                ps.setString(5, telephone);

                ps.executeUpdate();

                System.out.println("Compte Agent créé avec succès !");
                System.out.println("Email : " + email);
                System.out.println("Mot de passe : " + motDePasse);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}