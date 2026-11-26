package com.assurance.controller;

import java.io.IOException;

import com.assurance.service.PaiementService;
import com.assurance.util.ControleAcces;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Confirme l'encaissement d'un paiement en attente (bouton "Valider" de l'historique)
@WebServlet("/agent/valider-paiement")
public class ValiderPaiementServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private PaiementService paiementService = new PaiementService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!ControleAcces.exigerEmploye(request, response)) {
            return;
        }

        try {
            paiementService.validerPaiement(ControleAcces.lireId(request.getParameter("id")));
            ControleAcces.memoriserMessage(request, "Paiement validé.");

        } catch (IllegalArgumentException e) {
            ControleAcces.memoriserMessage(request, "Validation impossible : " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/agent/paiements");
    }
}
