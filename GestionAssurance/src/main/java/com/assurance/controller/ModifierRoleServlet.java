package com.assurance.controller;

import java.io.IOException;

import com.assurance.service.UtilisateurService;
import com.assurance.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/modifier-role")
public class ModifierRoleServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UtilisateurService utilisateurService = new UtilisateurService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("utilisateurId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String roleConnecte = (String) session.getAttribute("role");

        if (!"ADMIN".equalsIgnoreCase(roleConnecte)
                && !"ADMINISTRATEUR".equalsIgnoreCase(roleConnecte)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            String nouveauRole = request.getParameter("role");

            utilisateurService.changerRole(id, nouveauRole);

            session.setAttribute("messageSucces",
                    "Le rôle de l'utilisateur a été modifié avec succès.");

        } catch (NumberFormatException e) {

            session.setAttribute("messageErreur", "Identifiant utilisateur invalide.");

        } catch (ServiceException e) {

            session.setAttribute("messageErreur", e.getMessage());

        } catch (Exception e) {

            getServletContext().log("Erreur lors de la modification du rôle.", e);
            session.setAttribute("messageErreur", "Une erreur est survenue.");
        }

        response.sendRedirect(request.getContextPath() + "/admin/roles");
    }
}