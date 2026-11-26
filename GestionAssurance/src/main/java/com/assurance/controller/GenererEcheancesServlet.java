package com.assurance.controller;

import java.io.IOException;

import com.assurance.model.Contrat;
import com.assurance.service.ContratService;
import com.assurance.service.EcheanceService;
import com.assurance.util.ControleAcces;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Génère l'échéancier d'un contrat depuis la page des échéances (bouton "Générer").
// Utile tant que la génération n'est pas appelée automatiquement à l'activation.
@WebServlet("/agent/generer-echeances")
public class GenererEcheancesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private EcheanceService echeanceService = new EcheanceService();
    private ContratService contratService = new ContratService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!ControleAcces.exigerEmploye(request, response)) {
            return;
        }

        Long contratId = ControleAcces.lireId(request.getParameter("contratId"));
        Long periodicite = ControleAcces.lireId(request.getParameter("periodicite"));

        try {
            Contrat contrat = (contratId == null) ? null : contratService.trouverParId(contratId);

            if (contrat == null) {
                throw new IllegalArgumentException("Contrat introuvable.");
            }

            int mois = (periodicite == null)
                    ? EcheanceService.PERIODICITE_MENSUELLE
                    : periodicite.intValue();

            int nombre = echeanceService.genererEcheances(contrat, mois).size();

            ControleAcces.memoriserMessage(request,
                    nombre + " échéance(s) générée(s) pour le contrat "
                    + contrat.getNumeroContrat() + ".");

        } catch (IllegalArgumentException e) {
            ControleAcces.memoriserMessage(request, "Génération impossible : " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/agent/echeances"
                + (contratId != null ? "?contratId=" + contratId : ""));
    }
}
