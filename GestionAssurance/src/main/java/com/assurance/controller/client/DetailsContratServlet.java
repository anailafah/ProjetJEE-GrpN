package com.assurance.controller.client;

import java.io.IOException;

import com.assurance.model.Contrat;
import com.assurance.service.ContratService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/client/details-contrat")
public class DetailsContratServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ContratService contratService = new ContratService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Vérification de la connexion et du rôle
        if (session == null
                || session.getAttribute("utilisateurId") == null
                || !"CLIENT".equals(session.getAttribute("role"))) {

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long clientId = ((Number) session.getAttribute("utilisateurId")).longValue();

        // Récupération de l'identifiant du contrat
        String idParam = request.getParameter("id");

        try {
            if (idParam == null || idParam.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Identifiant du contrat manquant.");
                return;
            }

            Long contratId = Long.parseLong(idParam);

            Contrat contrat = contratService.trouverParId(contratId);

            if (contrat == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Contrat introuvable.");
                return;
            }

            // Sécurité : le client ne peut consulter que ses contrats
            if (contrat.getClient() == null
                    || !clientId.equals(contrat.getClient().getId())) {

                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Vous n'avez pas accès à ce contrat.");
                return;
            }

            request.setAttribute("contrat", contrat);

            request.getRequestDispatcher("/client/details-contrat.jsp")
                   .forward(request, response);

        } catch (NumberFormatException e) {

            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Identifiant de contrat invalide.");

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute("erreur",
                    "Impossible de charger les détails du contrat.");

            request.getRequestDispatcher("/client/details-contrat.jsp")
                   .forward(request, response);
        }
    }
}