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
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String motDePasse = request.getParameter("motDePasse");

        if (email == null || motDePasse == null
                || email.isBlank() || motDePasse.isBlank()) {

            request.setAttribute("erreur", "Veuillez remplir tous les champs.");
            doGet(request, response);
            return;
        }

        String sql = "SELECT id, nom, prenom, email, mot_de_passe, "
                   + "type_utilisateur, actif "
                   + "FROM utilisateurs WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String motDePasseEnregistre = rs.getString("mot_de_passe");

                    boolean motDePasseCorrect =
                            motDePasseEnregistre != null
                            && motDePasseEnregistre.startsWith("$2")
                            && BCrypt.checkpw(motDePasse, motDePasseEnregistre);

                    if (!motDePasseCorrect) {
                        request.setAttribute("erreur",
                                "Adresse e-mail ou mot de passe incorrect.");
                        doGet(request, response);
                        return;
                    }

                    if (!rs.getBoolean("actif")) {
                        request.setAttribute("erreur",
                                "Votre compte est désactivé.");
                        doGet(request, response);
                        return;
                    }

                    String type = rs.getString("type_utilisateur");

                    if (type == null ||
                            !(type.equalsIgnoreCase("CLIENT")
                            || type.equalsIgnoreCase("AGENT")
                            || type.equalsIgnoreCase("ADMIN")
                            || type.equalsIgnoreCase("ADMINISTRATEUR"))) {

                        request.setAttribute("erreur",
                                "Profil utilisateur non reconnu.");
                        doGet(request, response);
                        return;
                    }

                    HttpSession ancienneSession = request.getSession(false);

                    if (ancienneSession != null) {
                        ancienneSession.invalidate();
                    }

                    HttpSession session = request.getSession(true);

                    session.setAttribute("utilisateurId", rs.getLong("id"));
                    session.setAttribute("nom", rs.getString("nom"));
                    session.setAttribute("prenom", rs.getString("prenom"));
                    session.setAttribute("email", rs.getString("email"));
                    session.setAttribute("role", type.toUpperCase());

                    session.setMaxInactiveInterval(30 * 60);

                    String contextPath = request.getContextPath();

                    if ("CLIENT".equalsIgnoreCase(type)) {
                        response.sendRedirect(contextPath + "/client/dashboard");
                    } else if ("AGENT".equalsIgnoreCase(type)) {
                        response.sendRedirect(contextPath + "/agent/dashboard");
                    } else {
                        response.sendRedirect(contextPath + "/admin/dashboard");
                    }

                } else {
                    request.setAttribute("erreur",
                            "Adresse e-mail ou mot de passe incorrect.");
                    doGet(request, response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            request.setAttribute("erreur",
                    "Une erreur est survenue lors de la connexion.");
            doGet(request, response);
        }
    }
}
