package com.assurance.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.assurance.model.Paiement;
import com.assurance.service.PaiementService;
import com.assurance.util.ControleAcces;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Historique des paiements côté agent.
// Filtres facultatifs dans l'URL : ?statut=EFFECTUE|EN_ATTENTE et ?contratId=12
@WebServlet("/agent/paiements")
public class HistoriquePaiementServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FORMAT_DATE =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private PaiementService paiementService = new PaiementService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!ControleAcces.exigerEmploye(request, response)) {
            return;
        }

        ControleAcces.transfererMessage(request);

        String statut = request.getParameter("statut");
        Long contratId = ControleAcces.lireId(request.getParameter("contratId"));

        List<Paiement> paiements;

        try {
            // Point de départ : par contrat si demandé, sinon tout l'historique
            paiements = (contratId != null)
                    ? paiementService.historiqueParContrat(contratId)
                    : paiementService.listerHistorique();

        } catch (IllegalArgumentException e) {
            request.setAttribute("erreur", e.getMessage());
            paiements = List.of();
        }

        // Filtre par statut (s'ajoute au filtre par contrat)
        if (statut != null && !statut.isBlank()) {
            paiements = paiements.stream()
                    .filter(p -> statut.equals(p.getStatut()))
                    .collect(Collectors.toList());
        }

        request.setAttribute("paiements", paiements);
        request.setAttribute("totalEncaisse", totalEncaisse(paiements));
        request.setAttribute("statutChoisi", statut);
        request.setAttribute("contratIdChoisi", contratId);
        request.setAttribute("formatDate", FORMAT_DATE);

        request.getRequestDispatcher("/agent/paiements.jsp")
               .forward(request, response);
    }

    // Somme des paiements réellement encaissés dans la liste affichée
    private BigDecimal totalEncaisse(List<Paiement> paiements) {
        return paiements.stream()
                .filter(p -> PaiementService.STATUT_EFFECTUE.equals(p.getStatut()))
                .map(Paiement::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
