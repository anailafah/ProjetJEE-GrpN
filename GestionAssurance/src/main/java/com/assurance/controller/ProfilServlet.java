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

@WebServlet("/profil")
public class ProfilServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UtilisateurService utilisateurService = new UtilisateurService();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utilisateurId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long id = ((Number) session.getAttribute("utilisateurId")).longValue();
        Utilisateur utilisateur = utilisateurDAO.trouverParId(id);

        if (utilisateur == null) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = utilisateur instanceof Administrateur ? "Administrateur"
                    : utilisateur instanceof Agent ? "Agent" : "Client";

        String roleSession = String.valueOf(session.getAttribute("role"));
        String dashboard = "ADMIN".equals(roleSession) ? "/admin/dashboard"
                         : "AGENT".equals(roleSession) ? "/agent/dashboard"
                         : "/client/dashboard";

        String csrf = (String) session.getAttribute("csrfToken");
        if (csrf == null) {
            csrf = UUID.randomUUID().toString();
            session.setAttribute("csrfToken", csrf);
        }

        // Messages flash (affichés une seule fois)
        Object succes = session.getAttribute("flashSucces");
        if (succes != null) {
            request.setAttribute("succes", succes);
            session.removeAttribute("flashSucces");
        }
        Object erreur = session.getAttribute("flashErreur");
        if (erreur != null) {
            request.setAttribute("erreur", erreur);
            session.removeAttribute("flashErreur");
        }

        request.setAttribute("utilisateur", utilisateur);
        request.setAttribute("roleLibelle", role);
        request.setAttribute("urlDashboard", dashboard);
        request.setAttribute("csrfToken", csrf);

        request.getRequestDispatcher("/WEB-INF/views/profil.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utilisateurId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Vérification du jeton CSRF
        String recu = request.getParameter("csrf");
        String attendu = (String) session.getAttribute("csrfToken");
        if (attendu == null || !attendu.equals(recu)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Jeton de sécurité invalide.");
            return;
        }

        // L'identifiant vient de la SESSION, jamais du formulaire :
        // un utilisateur ne peut modifier que son propre compte.
        Long id = ((Number) session.getAttribute("utilisateurId")).longValue();
        String action = request.getParameter("action");

        try {
            if ("infos".equals(action)) {
                Utilisateur modifie = utilisateurService.modifierInformationsPersonnelles(
                        id,
                        request.getParameter("nom"),
                        request.getParameter("prenom"),
                        request.getParameter("telephone"));

                // On met à jour le nom affiché dans la session
                session.setAttribute("nom", modifie.getNom());
                session.setAttribute("prenom", modifie.getPrenom());
                session.setAttribute("flashSucces", "Informations mises à jour.");

            } else if ("motdepasse".equals(action)) {
                String ancien = request.getParameter("ancienMotDePasse");
                String nouveau = request.getParameter("nouveauMotDePasse");
                String confirmation = request.getParameter("confirmation");

                if (nouveau == null || !nouveau.equals(confirmation)) {
                    throw new ServiceException("La confirmation ne correspond pas au nouveau mot de passe.");
                }

                utilisateurService.changerMotDePasse(id, ancien, nouveau);
                session.setAttribute("flashSucces", "Mot de passe modifié.");

            } else {
                session.setAttribute("flashErreur", "Action inconnue.");
            }

        } catch (ServiceException e) {
            session.setAttribute("flashErreur", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/profil");
    }
}