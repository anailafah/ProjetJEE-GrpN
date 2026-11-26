package com.assurance.controller;

import java.io.IOException;
import java.util.UUID;

import com.assurance.dao.UtilisateurDAO;
import com.assurance.model.Administrateur;
import com.assurance.model.Agent;
import com.assurance.model.Utilisateur;
import com.assurance.service.ServiceException;
import com.assurance.service.UtilisateurService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/modifier-utilisateur")
public class ModifierUtilisateurServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UtilisateurService utilisateurService = new UtilisateurService();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        afficherFormulaire(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Vérification du jeton CSRF
        String recu = request.getParameter("csrf");
        String attendu = (String) session.getAttribute("csrfToken");
        if (attendu == null || !attendu.equals(recu)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Jeton de sécurité invalide.");
            return;
        }

        try {
            Long id = Long.valueOf(request.getParameter("id"));
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String telephone = request.getParameter("telephone");

            utilisateurService.modifierInformationsPersonnelles(id, nom, prenom, telephone);

            session.setAttribute("flashSucces", "Informations mises à jour.");
            response.sendRedirect(request.getContextPath() + "/admin/utilisateurs");

        } catch (NumberFormatException e) {
            session.setAttribute("flashErreur", "Identifiant invalide.");
            response.sendRedirect(request.getContextPath() + "/admin/utilisateurs");

        } catch (ServiceException e) {
            request.setAttribute("erreur", e.getMessage());
            afficherFormulaire(request, response);
        }
    }

    private void afficherFormulaire(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        Utilisateur utilisateur = null;
        try {
            Long id = Long.valueOf(request.getParameter("id"));
            utilisateur = utilisateurDAO.trouverParId(id);
        } catch (NumberFormatException e) {
            // identifiant absent ou invalide : traité plus bas
        }

        if (utilisateur == null) {
            session.setAttribute("flashErreur", "Utilisateur introuvable.");
            response.sendRedirect(request.getContextPath() + "/admin/utilisateurs");
            return;
        }

        String role = utilisateur instanceof Administrateur ? "Administrateur"
                    : utilisateur instanceof Agent ? "Agent" : "Client";

        String csrf = (String) session.getAttribute("csrfToken");
        if (csrf == null) {
            csrf = UUID.randomUUID().toString();
            session.setAttribute("csrfToken", csrf);
        }

        request.setAttribute("utilisateur", utilisateur);
        request.setAttribute("roleLibelle", role);
        request.setAttribute("csrfToken", csrf);

        request.getRequestDispatcher("/WEB-INF/views/admin/modifier-utilisateur.jsp")
               .forward(request, response);
    }
}