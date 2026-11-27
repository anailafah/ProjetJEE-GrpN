package com.assurance.controller;

import java.io.IOException;
import java.math.BigDecimal;

import com.assurance.model.Echeance;
import com.assurance.service.EcheanceService;
import com.assurance.service.PaiementService;
import com.assurance.util.ControleAcces;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// GET  ?echeanceId=5 : formulaire de paiement d'une échéance
// POST              : enregistre le paiement (encaissé ou en attente)
@WebServlet("/agent/enregistrer-paiement")
public class EnregistrerPaiementServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String VUE = "/agent/enregistrer-paiement.jsp";

    private EcheanceService echeanceService = new EcheanceService();
    private PaiementService paiementService = new PaiementService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!ControleAcces.exigerEmploye(request, response)) {
            return;
        }

        if (!preparerFormulaire(request, ControleAcces.lireId(request.getParameter("echeanceId")))) {
            response.sendRedirect(request.getContextPath() + "/agent/echeances");
            return;
        }

        request.getRequestDispatcher(VUE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!ControleAcces.exigerEmploye(request, response)) {
            return;
        }

        request.setCharacterEncoding("UTF-8");

        Long echeanceId = ControleAcces.lireId(request.getParameter("echeanceId"));
        String mode = request.getParameter("modePaiement");
        String reference = request.getParameter("reference");
        boolean enAttente = "EN_ATTENTE".equals(request.getParameter("encaissement"));

        try {
            BigDecimal montant = lireMontant(request.getParameter("montant"));

            if (enAttente) {
                paiementService.declarerPaiementEnAttente(echeanceId, montant, mode, reference);
            } else {
                paiementService.enregistrerPaiement(echeanceId, montant, mode, reference);
            }

            Echeance echeance = echeanceService.trouverParId(echeanceId);

            ControleAcces.memoriserMessage(request, enAttente
                    ? "Paiement de " + montant + " € enregistré en attente de validation."
                    : "Paiement de " + montant + " € enregistré.");

            response.sendRedirect(request.getContextPath()
                    + "/agent/echeances?contratId=" + echeance.getContrat().getId());

        } catch (IllegalArgumentException e) {
            // Erreur de saisie : on réaffiche le formulaire avec les valeurs tapées
            request.setAttribute("erreur", e.getMessage());

            if (!preparerFormulaire(request, echeanceId)) {
                response.sendRedirect(request.getContextPath() + "/agent/echeances");
                return;
            }

            request.getRequestDispatcher(VUE).forward(request, response);
        }
    }

    // Charge l'échéance, son reste à payer et la liste des modes.
    // Renvoie false si l'échéance n'existe pas.
    private boolean preparerFormulaire(HttpServletRequest request, Long echeanceId) {

        if (echeanceId == null) {
            return false;
        }

        Echeance echeance = echeanceService.trouverParId(echeanceId);

        if (echeance == null) {
            return false;
        }

        request.setAttribute("echeance", echeance);
        request.setAttribute("reste", paiementService.resteAPayer(echeance));
        request.setAttribute("modes", PaiementService.MODES_PAIEMENT);
        return true;
    }

    // Accepte "50", "50.5" et "50,50"
    private BigDecimal lireMontant(String valeur) {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("Le montant est obligatoire.");
        }

        try {
            return new BigDecimal(valeur.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Montant invalide.");
        }
    }
}
