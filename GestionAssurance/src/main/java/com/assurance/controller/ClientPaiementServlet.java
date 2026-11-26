package com.assurance.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.assurance.model.Echeance;
import com.assurance.service.EcheanceService;
import com.assurance.service.PaiementService;
import com.assurance.util.ControleAcces;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Espace client : ses échéances et l'historique de ses paiements.
// L'identifiant vient de la session, jamais de l'URL : un client ne peut pas
// afficher les données d'un autre en changeant un paramètre.
@WebServlet("/client/paiements")
public class ClientPaiementServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FORMAT_DATE =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private EcheanceService echeanceService = new EcheanceService();
    private PaiementService paiementService = new PaiementService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long clientId = ControleAcces.exigerClient(request, response);

        if (clientId == null) {
            return;
        }

        try {
            echeanceService.gererRetards();

            List<Echeance> echeances = echeanceService.listerParClient(clientId);

            Map<Long, BigDecimal> restes = new HashMap<>();
            for (Echeance e : echeances) {
                restes.put(e.getId(), paiementService.resteAPayer(e));
            }

            request.setAttribute("echeances", echeances);
            request.setAttribute("restes", restes);
            request.setAttribute("paiements", paiementService.historiqueParClient(clientId));
            request.setAttribute("formatDate", FORMAT_DATE);

        } catch (RuntimeException e) {
            e.printStackTrace();
            request.setAttribute("erreur", "Impossible de charger vos paiements.");
        }

        request.getRequestDispatcher("/client/paiements.jsp")
               .forward(request, response);
    }
}
