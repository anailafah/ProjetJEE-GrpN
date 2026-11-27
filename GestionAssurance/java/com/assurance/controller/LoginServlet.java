package com.assurance.controller;

import java.io.IOException;

import com.assurance.model.Administrateur;
import com.assurance.model.Agent;
import com.assurance.model.Client;
import com.assurance.model.Utilisateur;
import com.assurance.service.ServiceException;
import com.assurance.service.UtilisateurService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Connexion : la vérification (email, mot de passe BCrypt, compte actif) est faite par
 * UtilisateurService.authentifier. Ce servlet crée seulement la session et redirige selon le rôle.
 *
 * Attributs de session (lus par les dashboards) : utilisateurId, nom, prenom, email, role.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UtilisateurService service = new UtilisateurService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String motDePasse = request.getParameter("motDePasse");

        try {
            Utilisateur utilisateur = service.authentifier(email, motDePasse);
            String role = roleDe(utilisateur);

            // Nouvelle session après connexion (protège contre la fixation de session)
            HttpSession ancienneSession = request.getSession(false);
            if (ancienneSession != null) {
                ancienneSession.invalidate();
            }
            HttpSession session = request.getSession(true);

            session.setAttribute("utilisateurId", utilisateur.getId());
            session.setAttribute("nom", utilisateur.getNom());
            session.setAttribute("prenom", utilisateur.getPrenom());
            session.setAttribute("email", utilisateur.getEmail());
            session.setAttribute("role", role);

            session.setMaxInactiveInterval(30 * 60);

            String contextPath = request.getContextPath();
            switch (role) {
                case "CLIENT":
                    response.sendRedirect(contextPath + "/client/dashboard");
                    break;
                case "AGENT":
                    response.sendRedirect(contextPath + "/agent/dashboard");
                    break;
                default:
                    response.sendRedirect(contextPath + "/admin/dashboard");
                    break;
            }

        } catch (ServiceException e) {
            request.setAttribute("erreur", e.getMessage());
            doGet(request, response);
        } catch (RuntimeException e) {
            getServletContext().log("Erreur lors de la connexion", e);
            request.setAttribute("erreur", "Une erreur est survenue lors de la connexion.");
            doGet(request, response);
        }
    }

    /** Rôle stocké en session : CLIENT, AGENT ou ADMIN (mêmes valeurs que type_utilisateur en base). */
    private static String roleDe(Utilisateur u) {
        if (u instanceof Client) {
            return "CLIENT";
        }
        if (u instanceof Agent) {
            return "AGENT";
        }
        if (u instanceof Administrateur) {
            return "ADMIN";
        }
        throw new ServiceException("Profil utilisateur non reconnu.");
    }
}