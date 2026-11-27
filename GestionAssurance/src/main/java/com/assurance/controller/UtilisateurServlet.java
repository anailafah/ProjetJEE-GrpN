package com.assurance.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

@WebServlet("/admin/utilisateurs")
public class UtilisateurServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UtilisateurService service = new UtilisateurService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String q = request.getParameter("q");
        String role = request.getParameter("role");

        try {
            List<Utilisateur> liste = service.rechercher(q, role);
            List<Map<String, Object>> lignes = new ArrayList<>();

            for (Utilisateur u : liste) {
                Map<String, Object> m = new HashMap<>();
                m.put("id", u.getId());
                m.put("nom", u.getNom());
                m.put("prenom", u.getPrenom());
                m.put("email", u.getEmail());
                m.put("telephone", u.getTelephone());
                m.put("actif", u.isActif());
                m.put("role", u instanceof Administrateur ? "ADMIN"
                            : u instanceof Agent ? "AGENT" : "CLIENT");
                lignes.add(m);
            }
            request.setAttribute("utilisateurs", lignes);
        } catch (ServiceException e) {
            request.setAttribute("erreur", e.getMessage());
        }

        // Jeton CSRF : créé une fois par session
        String csrf = (String) session.getAttribute("csrfToken");
        if (csrf == null) {
            csrf = UUID.randomUUID().toString();
            session.setAttribute("csrfToken", csrf);
        }

        request.setAttribute("q", q == null ? "" : q);
        request.setAttribute("roleFiltre", role == null ? "" : role);
        request.setAttribute("csrfToken", csrf);
        request.setAttribute("utilisateurCourantId", session.getAttribute("utilisateurId"));

        // Messages flash (affichés une seule fois)
        Object succes = session.getAttribute("flashSucces");
        if (succes != null) {
            request.setAttribute("succes", succes);
            session.removeAttribute("flashSucces");
        }
        Object erreurFlash = session.getAttribute("flashErreur");
        if (erreurFlash != null) {
            request.setAttribute("erreur", erreurFlash);
            session.removeAttribute("flashErreur");
        }

        request.getRequestDispatcher("/WEB-INF/views/admin/utilisateurs.jsp")
               .forward(request, response);
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

        String action = request.getParameter("action");

        try {
            long id = Long.parseLong(request.getParameter("id"));
            Object courant = session.getAttribute("utilisateurId");

            if ("activer".equals(action)) {
                service.activerCompte(id);
                session.setAttribute("flashSucces", "Compte activé.");

            } else if ("desactiver".equals(action)) {
                if (courant != null && ((Number) courant).longValue() == id) {
                    session.setAttribute("flashErreur",
                            "Vous ne pouvez pas désactiver votre propre compte.");
                } else {
                    service.desactiverCompte(id);
                    session.setAttribute("flashSucces", "Compte désactivé.");
                }

            } else {
                session.setAttribute("flashErreur", "Action inconnue.");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("flashErreur", "Identifiant invalide.");
        } catch (ServiceException e) {
            session.setAttribute("flashErreur", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/utilisateurs");
    }
}