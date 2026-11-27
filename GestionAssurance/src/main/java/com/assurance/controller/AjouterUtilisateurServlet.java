package com.assurance.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import com.assurance.service.ServiceException;
import com.assurance.service.UtilisateurService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/ajouter-utilisateur")
public class AjouterUtilisateurServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UtilisateurService utilisateurService = new UtilisateurService();

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

        String role = request.getParameter("role");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String motDePasse = request.getParameter("motDePasse");
        String telephone = vide(request.getParameter("telephone"));

        try {
            if ("CLIENT".equals(role)) {
                String adresse = vide(request.getParameter("adresse"));
                String dateTexte = vide(request.getParameter("dateNaissance"));
                LocalDate dateNaissance = null;
                if (dateTexte != null) {
                    try {
                        dateNaissance = LocalDate.parse(dateTexte);
                    } catch (DateTimeParseException e) {
                        throw new ServiceException("Date de naissance invalide.");
                    }
                }
                utilisateurService.creerClient(nom, prenom, email, motDePasse,
                        telephone, adresse, dateNaissance);

            } else if ("AGENT".equals(role)) {
                String matricule = vide(request.getParameter("matricule"));
                String serviceAgent = vide(request.getParameter("serviceAgent"));
                utilisateurService.creerAgent(nom, prenom, email, motDePasse,
                        telephone, matricule, serviceAgent);

            } else if ("ADMIN".equals(role)) {
                String niveauAcces = vide(request.getParameter("niveauAcces"));
                utilisateurService.creerAdministrateur(nom, prenom, email, motDePasse,
                        telephone, niveauAcces);

            } else {
                throw new ServiceException("Veuillez choisir un rôle.");
            }

            session.setAttribute("flashSucces", "Compte créé : " + email.trim());
            response.sendRedirect(request.getContextPath() + "/admin/utilisateurs");

        } catch (ServiceException e) {
            // On réaffiche le formulaire avec le message ; les champs sont repris via ${param.xxx}
            request.setAttribute("erreur", e.getMessage());
            afficherFormulaire(request, response);
        }
    }

    private void afficherFormulaire(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String csrf = (String) session.getAttribute("csrfToken");
        if (csrf == null) {
            csrf = UUID.randomUUID().toString();
            session.setAttribute("csrfToken", csrf);
        }
        request.setAttribute("csrfToken", csrf);

        request.getRequestDispatcher("/WEB-INF/views/admin/ajouter-utilisateur.jsp")
               .forward(request, response);
    }

    /** Transforme une chaîne vide en null. */
    private String vide(String s) {
        return (s == null || s.trim().isEmpty()) ? null : s.trim();
    }
}