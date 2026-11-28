package com.assurance.controller.client;

import java.io.IOException;

import com.assurance.model.Sinistre;
import com.assurance.service.SinistreService;
import com.assurance.util.ControleAcces;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Détail d'un sinistre du client : informations, pièces justificatives, commentaires de l'agent.
 * Un client ne peut ouvrir que ses propres sinistres.
 */
@WebServlet("/client/sinistre")
public class DetailSinistreClientServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private SinistreService sinistreService = new SinistreService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long clientId = ControleAcces.exigerClient(request, response);
        if (clientId == null) {
            return;
        }

        Long sinistreId = ControleAcces.lireId(request.getParameter("id"));

        if (sinistreId == null) {
            response.sendRedirect(request.getContextPath() + "/client/sinistres");
            return;
        }

        ControleAcces.transfererMessage(request);

        try {
            Sinistre sinistre = sinistreService.trouverPourClient(sinistreId, clientId);

            request.setAttribute("sinistre", sinistre);
            request.setAttribute("documents", sinistreService.listerDocuments(sinistreId));
            request.setAttribute("commentaires", sinistreService.listerCommentaires(sinistreId));

            request.getRequestDispatcher("/client/sinistre-detail.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Sinistre introuvable.");
        }
    }
}
