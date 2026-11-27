package com.assurance.controller.admin;

import java.io.IOException;
import java.time.LocalDate;

import com.assurance.service.UtilisateurService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//@WebServlet("/admin/ajouter-utilisateur")
public class AdminAjouterUtilisateurServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UtilisateurService utilisateurService = new UtilisateurService();

    private boolean verifierAdministrateur(HttpServletRequest request,
                                           HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("utilisateurId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equalsIgnoreCase(role)
                && !"ADMINISTRATEUR".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!verifierAdministrateur(request, response)) {
            return;
        }

        request.getRequestDispatcher("/admin/ajouter-utilisateur.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!verifierAdministrateur(request, response)) {
            return;
        }

        try {
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String email = request.getParameter("email");
            String telephone = request.getParameter("telephone");
            String motDePasse = request.getParameter("motDePasse");
            String role = request.getParameter("role");

            if (role == null) {
                throw new IllegalArgumentException("Veuillez choisir un rôle.");
            }

            switch (role.toUpperCase()) {

                case "CLIENT": {
                    String adresse = request.getParameter("adresse");
                    String dateNaissanceTexte = request.getParameter("dateNaissance");

                    LocalDate dateNaissance = null;

                    if (dateNaissanceTexte != null
                            && !dateNaissanceTexte.isBlank()) {
                        dateNaissance = LocalDate.parse(dateNaissanceTexte);
                    }

                    utilisateurService.creerClient(
                            nom, prenom, email, motDePasse,
                            telephone, adresse, dateNaissance
                    );
                    break;
                }

                case "AGENT": {
                    String matricule = request.getParameter("matricule");
                    String service = request.getParameter("service");

                    utilisateurService.creerAgent(
                            nom, prenom, email, motDePasse,
                            telephone, matricule, service
                    );
                    break;
                }

                case "ADMIN": {
                    String niveauAcces = request.getParameter("niveauAcces");

                    utilisateurService.creerAdministrateur(
                            nom, prenom, email, motDePasse,
                            telephone, niveauAcces
                    );
                    break;
                }

                default:
                    throw new IllegalArgumentException("Le rôle sélectionné est invalide.");
            }

            HttpSession session = request.getSession();
            session.setAttribute("messageSucces",
                    "L'utilisateur a été créé avec succès.");

            response.sendRedirect(
                    request.getContextPath() + "/admin/utilisateurs"
            );

        } catch (Exception e) {

            request.setAttribute("messageErreur", e.getMessage());

            request.getRequestDispatcher("/admin/ajouter-utilisateur.jsp")
                   .forward(request, response);
        }
    }
}