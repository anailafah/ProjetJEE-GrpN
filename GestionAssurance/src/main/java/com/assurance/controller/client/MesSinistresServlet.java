package com.assurance.controller.client;

import java.io.IOException;

import com.assurance.service.SinistreService;
import com.assurance.util.ControleAcces;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Liste des sinistres du client connecté : suivi de l'état et historique des déclarations.
 */
@WebServlet("/client/sinistres")
public class MesSinistresServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private SinistreService sinistreService = new SinistreService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long clientId = ControleAcces.exigerClient(request, response);
        if (clientId == null) {
            return;
        }

        ControleAcces.transfererMessage(request);

        try {
            request.setAttribute("sinistres", sinistreService.listerParClient(clientId));
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erreur", "Impossible de charger vos sinistres.");
        }

        request.getRequestDispatcher("/client/sinistres.jsp").forward(request, response);
    }
}
