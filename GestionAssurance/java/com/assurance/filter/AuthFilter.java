package com.assurance.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Protège les espaces privés :
 *  - /client/*  : rôle CLIENT
 *  - /agent/*   : rôle AGENT
 *  - /admin/*   : rôle ADMIN
 * Non connecté -> redirection vers /login ; mauvais rôle -> erreur 403.
 * Le rôle est lu dans la session (attribut "role", posé par LoginServlet).
 */
@WebFilter(urlPatterns = { "/client/*", "/agent/*", "/admin/*" })
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Empêche le navigateur de garder une page privée en cache (bouton "retour" après déconnexion)
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        HttpSession session = request.getSession(false);
        Object utilisateurId = (session == null) ? null : session.getAttribute("utilisateurId");
        Object role = (session == null) ? null : session.getAttribute("role");

        if (utilisateurId == null || role == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String chemin = request.getServletPath(); // ex. "/admin/dashboard"
        String roleRequis = roleRequis(chemin);

        if (roleRequis != null && !roleRequis.equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès interdit pour votre profil.");
            return;
        }

        chain.doFilter(request, response);
    }

    private String roleRequis(String chemin) {
        if (chemin.startsWith("/admin/")) {
            return "ADMIN";
        }
        if (chemin.startsWith("/agent/")) {
            return "AGENT";
        }
        if (chemin.startsWith("/client/")) {
            return "CLIENT";
        }
        return null;
    }
}