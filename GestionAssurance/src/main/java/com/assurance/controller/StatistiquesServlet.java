package com.assurance.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Lien générique "Statistiques" des menus : redirige vers la page de
 * statistiques correspondant au rôle de l'utilisateur connecté.
 * (Les statistiques ne sont jamais affichées sans être connecté.)
 */
@WebServlet("/statistiques")
public class StatistiquesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Object role = (session == null) ? null : session.getAttribute("role");
        String contexte = request.getContextPath();

        if ("ADMIN".equals(role)) {
            response.sendRedirect(contexte + "/admin/dashboard");
        } else if ("AGENT".equals(role)) {
            response.sendRedirect(contexte + "/agent/statistiques");
        } else if (role != null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé aux agents et administrateurs.");
        } else {
            response.sendRedirect(contexte + "/login");
        }
    }
}
