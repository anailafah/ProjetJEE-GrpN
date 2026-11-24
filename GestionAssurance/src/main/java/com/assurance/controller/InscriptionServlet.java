package com.assurance.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.mindrot.jbcrypt.BCrypt;

import com.assurance.util.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/inscription")
public class InscriptionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/inscription.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String telephone = request.getParameter("telephone");
        String motDePasse = request.getParameter("motDePasse");
        String confirmation = request.getParameter("confirmation");

        if (nom == null || prenom == null || email == null
                || motDePasse == null || confirmation == null
                || nom.isBlank() || prenom.isBlank() || email.isBlank()
                || motDePasse.isBlank() || confirmation.isBlank()) {
            request.setAttribute("erreur", "Veuillez remplir tous les champs obligatoires.");
            doGet(request, response);
            return;
        }

        nom = nom.trim();
        prenom = prenom.trim();
        email = email.trim().toLowerCase();

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
        	request.setAttribute("erreur", "Adresse e-mail invalide.");
            doGet(request, response);
            return;
        }

        if (motDePasse.length() < 8) {
            request.setAttribute("erreur", "Le mot de passe doit contenir au moins 8 caractères.");
            doGet(request, response);
            return;
        }

        if (!motDePasse.equals(confirmation)) {
            request.setAttribute("erreur", "Les mots de passe ne correspondent pas.");
            doGet(request, response);
            return;
        }

        String verification = "SELECT id FROM utilisateurs WHERE email = ?";
        String insertion = "INSERT INTO utilisateurs "
                + "(type_utilisateur, nom, prenom, email, mot_de_passe, telephone, date_creation, actif) "
                + "VALUES ('CLIENT', ?, ?, ?, ?, ?, NOW(), 1)";

        try (Connection conn = DatabaseConnection.getConnection()) {

            try (PreparedStatement verificationPs = conn.prepareStatement(verification)) {
                verificationPs.setString(1, email);

                try (ResultSet rs = verificationPs.executeQuery()) {
                    if (rs.next()) {
                        request.setAttribute("erreur", "Cette adresse e-mail est déjà utilisée.");
                        doGet(request, response);
                        return;
                    }
                }
            }

            String motDePasseHash = BCrypt.hashpw(motDePasse, BCrypt.gensalt());

            try (PreparedStatement insertionPs = conn.prepareStatement(insertion)) {
                insertionPs.setString(1, nom);
                insertionPs.setString(2, prenom);
                insertionPs.setString(3, email);
                insertionPs.setString(4, motDePasseHash);
                insertionPs.setString(5, telephone);

                insertionPs.executeUpdate();
            }

            response.sendRedirect(request.getContextPath() + "/login?inscription=success");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erreur",
                    "Une erreur est survenue lors de l'inscription. Vérifiez les informations saisies.");
            doGet(request, response);
        }
    }
}
