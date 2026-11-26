package com.assurance.util;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// Vérifications communes aux servlets Paiements / Échéances :
// une seule implémentation au lieu de recopier le même test dans chaque servlet.
public final class ControleAcces {

    private static final String CLE_MESSAGE = "messagePaiement";

    private ControleAcces() {
    }

    // Agent ou administrateur connecté. Sinon, redirige vers la connexion
    // et renvoie false : la servlet doit alors s'arrêter (return).
    public static boolean exigerEmploye(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String role = role(request);

        boolean autorise = "AGENT".equalsIgnoreCase(role)
                || "ADMIN".equalsIgnoreCase(role)
                || "ADMINISTRATEUR".equalsIgnoreCase(role);

        if (!autorise) {
            response.sendRedirect(request.getContextPath() + "/login");
        }

        return autorise;
    }

    // Client connecté. Renvoie son identifiant, ou null après redirection.
    public static Long exigerClient(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        if (!"CLIENT".equals(role(request))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }

        Object id = request.getSession(false).getAttribute("utilisateurId");
        return ((Number) id).longValue();
    }

    // Message affiché une seule fois sur la page suivante (après une redirection)
    public static void memoriserMessage(HttpServletRequest request, String message) {
        request.getSession().setAttribute(CLE_MESSAGE, message);
    }

    public static void transfererMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute(CLE_MESSAGE) != null) {
            request.setAttribute("succes", session.getAttribute(CLE_MESSAGE));
            session.removeAttribute(CLE_MESSAGE);
        }
    }

    // Paramètre numérique facultatif : null s'il est absent ou mal formé
    public static Long lireId(String valeur) {
        if (valeur == null || valeur.isBlank()) {
            return null;
        }

        try {
            return Long.valueOf(valeur.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String role(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("utilisateurId") == null) {
            return null;
        }

        return (String) session.getAttribute("role");
    }
}
