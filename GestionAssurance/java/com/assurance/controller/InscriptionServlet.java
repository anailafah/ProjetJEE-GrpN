package com.assurance.controller;

import java.io.IOException;

import com.assurance.service.ServiceException;
import com.assurance.service.UtilisateurService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Inscription publique : crée uniquement des comptes CLIENT.
 * Toute la validation et le hachage BCrypt sont faits par UtilisateurService
 * (accès à la base via Hibernate, comme le reste du projet).
 */
@WebServlet("/inscription")
public class InscriptionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final UtilisateurService service = new UtilisateurService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/inscription.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String telephone = request.getParameter("telephone");
        String motDePasse = request.getParameter("motDePasse");
        String confirmation = request.getParameter("confirmation");

        try {
            if (motDePasse == null || !motDePasse.equals(confirmation)) {
                throw new ServiceException("Les mots de passe ne correspondent pas.");
            }

            // Le rôle n'est jamais lu dans le formulaire : creerClient crée toujours un CLIENT.
            // Adresse et date de naissance sont facultatives : null si le formulaire ne les contient pas.
            service.creerClient(nom, prenom, email, motDePasse, telephone, null, null);

            response.sendRedirect(request.getContextPath() + "/login?inscription=success");

        } catch (ServiceException e) {
            request.setAttribute("erreur", e.getMessage());
            doGet(request, response);
        } catch (RuntimeException e) {
            getServletContext().log("Erreur lors de l'inscription", e);
            request.setAttribute("erreur",
                    "Une erreur technique est survenue. Réessayez plus tard.");
            doGet(request, response);
        }
    }
}