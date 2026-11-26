package com.assurance.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.assurance.model.Echeance;
import com.assurance.service.ContratService;
import com.assurance.service.EcheanceService;
import com.assurance.service.PaiementService;
import com.assurance.util.ControleAcces;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Échéances côté agent.
// Sans paramètre : les échéances impayées dont la date est dépassée.
// ?contratId=12  : l'échéancier complet d'un contrat.
@WebServlet("/agent/echeances")
public class EcheancesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private EcheanceService echeanceService = new EcheanceService();
    private PaiementService paiementService = new PaiementService();
    private ContratService contratService = new ContratService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!ControleAcces.exigerEmploye(request, response)) {
            return;
        }

        ControleAcces.transfererMessage(request);

        // Met à jour les statuts EN_RETARD à chaque consultation
        echeanceService.gererRetards();

        Long contratId = ControleAcces.lireId(request.getParameter("contratId"));
        List<Echeance> echeances;

        try {
            if (contratId != null) {
                echeances = echeanceService.listerParContrat(contratId);
                request.setAttribute("contrat", contratService.trouverParId(contratId));
            } else {
                echeances = echeanceService.detecterEcheancesImpayees();
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("erreur", e.getMessage());
            echeances = List.of();
        }

        // Reste à payer de chaque échéance, lu dans la JSP avec ${restes[e.id]}
        Map<Long, BigDecimal> restes = new HashMap<>();
        for (Echeance e : echeances) {
            restes.put(e.getId(), paiementService.resteAPayer(e));
        }

        request.setAttribute("echeances", echeances);
        request.setAttribute("restes", restes);
        request.setAttribute("contratIdChoisi", contratId);
        request.setAttribute("contrats", contratService.lister());

        request.getRequestDispatcher("/agent/echeances.jsp")
               .forward(request, response);
    }
}
