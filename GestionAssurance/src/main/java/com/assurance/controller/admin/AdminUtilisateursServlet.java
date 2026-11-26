package com.assurance.controller.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.assurance.dao.UtilisateurDAO;
import com.assurance.model.Administrateur;
import com.assurance.model.Agent;
import com.assurance.model.Utilisateur;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/utilisateurs")
public class AdminUtilisateursServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Vérification de la connexion
        if (session == null || session.getAttribute("utilisateurId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Vérification du rôle
        String roleSession = (String) session.getAttribute("role");

        if (!"ADMIN".equalsIgnoreCase(roleSession)
                && !"ADMINISTRATEUR".equalsIgnoreCase(roleSession)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            String recherche = request.getParameter("recherche");

            // Récupération des utilisateurs
            List<Utilisateur> liste;

            if (recherche != null && !recherche.trim().isEmpty()) {
                liste = utilisateurDAO.rechercher(recherche.trim());
            } else {
                liste = utilisateurDAO.lister();
            }

            // Préparation des données pour la JSP
            List<Map<String, Object>> utilisateurs = new ArrayList<>();

            for (Utilisateur u : liste) {

                Map<String, Object> utilisateur = new HashMap<>();

                utilisateur.put("id", u.getId());
                utilisateur.put("nom", u.getNom());
                utilisateur.put("prenom", u.getPrenom());
                utilisateur.put("email", u.getEmail());
                utilisateur.put("telephone", u.getTelephone());
                utilisateur.put("actif", u.isActif());

                // Détermination du rôle à partir de la classe Java
                String role;

                if (u instanceof Administrateur) {
                    role = "ADMIN";
                } else if (u instanceof Agent) {
                    role = "AGENT";
                } else {
                    role = "CLIENT";
                }

                utilisateur.put("role", role);

                utilisateurs.add(utilisateur);
            }

            request.setAttribute("utilisateurs", utilisateurs);

            request.getRequestDispatcher("/admin/utilisateurs.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            throw new ServletException(
                "Erreur lors du chargement des utilisateurs.", e
            );
        }
    }
}