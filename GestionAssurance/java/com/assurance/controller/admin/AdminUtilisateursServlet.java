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
import com.assurance.service.UtilisateurService;

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
    private final UtilisateurService utilisateurService = new UtilisateurService();

    // Vérification des droits administrateur
    private boolean verifierAdmin(HttpServletRequest request,
                                  HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("utilisateurId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        String roleSession = (String) session.getAttribute("role");

        if (!"ADMIN".equalsIgnoreCase(roleSession)
                && !"ADMINISTRATEUR".equalsIgnoreCase(roleSession)) {

            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }

    // Afficher et filtrer les utilisateurs
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (!verifierAdmin(request, response)) {
            return;
        }

        try {

            String recherche = request.getParameter("recherche");
            String roleFiltre = request.getParameter("role");

            List<Utilisateur> liste = utilisateurService.rechercher(
                    recherche,
                    roleFiltre
            );

            List<Map<String, Object>> utilisateurs = new ArrayList<>();

            for (Utilisateur u : liste) {

                Map<String, Object> utilisateur = new HashMap<>();

                utilisateur.put("id", u.getId());
                utilisateur.put("nom", u.getNom());
                utilisateur.put("prenom", u.getPrenom());
                utilisateur.put("email", u.getEmail());
                utilisateur.put("telephone", u.getTelephone());
                utilisateur.put("actif", u.isActif());

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
            request.setAttribute("recherche", recherche);
            request.setAttribute("roleFiltre", roleFiltre);

            request.getRequestDispatcher("/admin/utilisateurs.jsp")
                   .forward(request, response);

        } catch (Exception e) {

            throw new ServletException(
                    "Erreur lors du chargement des utilisateurs.", e);
        }
    }

    // Désactiver un utilisateur
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        if (!verifierAdmin(request, response)) {
            return;
        }

        try {

            String idParametre = request.getParameter("id");

            if (idParametre == null || idParametre.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Identifiant utilisateur manquant.");
            }

            Long id = Long.parseLong(idParametre);

            utilisateurService.desactiverCompte(id);

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/utilisateurs?success=desactivation"
            );

        } catch (Exception e) {

            HttpSession session = request.getSession();

            session.setAttribute("messageErreur", e.getMessage());

            response.sendRedirect(
                    request.getContextPath() + "/admin/utilisateurs"
            );
        }
    }
}