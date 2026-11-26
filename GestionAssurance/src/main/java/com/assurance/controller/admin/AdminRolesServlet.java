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

@WebServlet("/admin/roles")
public class AdminRolesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("utilisateurId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String roleConnecte = (String) session.getAttribute("role");

        if (!"ADMIN".equalsIgnoreCase(roleConnecte)
                && !"ADMINISTRATEUR".equalsIgnoreCase(roleConnecte)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            List<Utilisateur> liste = utilisateurDAO.lister();
            List<Map<String, Object>> utilisateurs = new ArrayList<>();

            for (Utilisateur u : liste) {
                Map<String, Object> ligne = new HashMap<>();

                ligne.put("id", u.getId());
                ligne.put("nom", u.getNom());
                ligne.put("prenom", u.getPrenom());
                ligne.put("email", u.getEmail());

                String role;

                if (u instanceof Administrateur) {
                    role = "ADMIN";
                } else if (u instanceof Agent) {
                    role = "AGENT";
                } else {
                    role = "CLIENT";
                }

                ligne.put("role", role);
                utilisateurs.add(ligne);
            }

            request.setAttribute("utilisateurs", utilisateurs);

            request.getRequestDispatcher("/admin/roles.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Erreur lors du chargement des rôles.", e);
        }
    }
}