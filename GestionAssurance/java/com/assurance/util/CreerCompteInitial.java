package com.assurance.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;
import org.mindrot.jbcrypt.BCrypt;

public class CreerCompteInitial {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Nom : ");
            String nom = scanner.nextLine();

            System.out.print("Prénom : ");
            String prenom = scanner.nextLine();

            System.out.print("Email : ");
            String email = scanner.nextLine();

            System.out.print("Téléphone : ");
            String telephone = scanner.nextLine();

            System.out.print("Mot de passe : ");
            String motDePasse = scanner.nextLine();

            String hash = BCrypt.hashpw(
                motDePasse,
                BCrypt.gensalt()
            );

            String sql = """
                INSERT INTO utilisateurs
                (type_utilisateur, nom, prenom, email,
                 mot_de_passe, telephone, date_creation, actif)
                VALUES ('ADMIN', ?, ?, ?, ?, ?, NOW(), 1)
                """;

            try (Connection connexion =
                     DatabaseConnection.getConnection();
                 PreparedStatement ps =
                     connexion.prepareStatement(sql)) {

                ps.setString(1, nom);
                ps.setString(2, prenom);
                ps.setString(3, email);
                ps.setString(4, hash);
                ps.setString(5, telephone);

                ps.executeUpdate();

                System.out.println(
                    "Compte administrateur créé avec succès !"
                );
            }

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        scanner.close();
    }
}