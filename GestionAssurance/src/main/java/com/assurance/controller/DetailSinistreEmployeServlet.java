package com.assurance.controller;

import java.io.IOException;
import java.math.BigDecimal;

import com.assurance.model.Sinistre;
import com.assurance.model.StatutSinistre;
import com.assurance.service.SinistreService;
import com.assurance.util.ControleAcces;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Détail d'un sinistre côté employé.
 *  - /agent/sinistre : consultation + traitement (statut, commentaire, indemnisation)
 *  - /admin/sinistre : consultation seule (le cahier des charges prévoit que l'admin consulte)
 */
@WebServlet({ "/agent/sinistre", "/admin/sinistre" })
public class DetailSinistreEmployeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String CLE_ERREUR = "erreurSinistre";

    private SinistreService sinistreService = new SinistreService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!ControleAcces.exigerEmploye(request, response)) {
            return;
        }

        Long sinistreId = ControleAcces.lireId(request.getParameter("id"));
        String espace = espace(request);

        if (sinistreId == null) {
            response.sendRedirect(request.getContextPath() + "/" + espace + "/sinistres");
            return;
        }

        ControleAcces.transfererMessage(request);
        transfererErreur(request);

        Sinistre sinistre;

        try {
            sinistre = sinistreService.trouverParId(sinistreId);
        } catch (IllegalArgumentException e) {
            sinistre = null;
        }

        if (sinistre == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Sinistre introuvable.");
            return;
        }

        request.setAttribute("espace", espace);
        request.setAttribute("peutTraiter", "agent".equals(espace));
        request.setAttribute("sinistre", sinistre);
        request.setAttribute("documents", sinistreService.listerDocuments(sinistreId));
        request.setAttribute("commentaires", sinistreService.listerCommentaires(sinistreId));
        request.setAttribute("statutsPossibles", sinistreService.statutsPossibles(sinistre.getStatut()));

        request.getRequestDispatcher("/WEB-INF/views/sinistres/detail.jsp").forward(request, response);
    }

    /**
     * Actions de l'agent. Champ caché "action" : statut | commentaire | indemniser.
     * Après chaque action : redirection vers la page de détail (évite le double envoi en cas de F5).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!ControleAcces.exigerEmploye(request, response)) {
            return;
        }

        if (!"agent".equals(espace(request))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "L'administrateur consulte les sinistres mais ne les traite pas.");
            return;
        }

        Long sinistreId = ControleAcces.lireId(request.getParameter("id"));

        if (sinistreId == null) {
            response.sendRedirect(request.getContextPath() + "/agent/sinistres");
            return;
        }

        Long agentId = ((Number) request.getSession(false).getAttribute("utilisateurId")).longValue();
        String action = request.getParameter("action");

        try {
            if ("statut".equals(action)) {
                StatutSinistre statut = lireStatut(request.getParameter("statut"));

                // Commentaire facultatif saisi avec le changement de statut (vérifié avant de changer le statut)
                String motif = request.getParameter("commentaire");
                if (motif != null && motif.trim().length() > 2000) {
                    throw new IllegalArgumentException("Le commentaire ne doit pas dépasser 2000 caractères.");
                }

                sinistreService.changerStatut(sinistreId, statut);

                if (motif != null && !motif.isBlank()) {
                    sinistreService.ajouterCommentaire(sinistreId, agentId,
                            "[" + statut.getLibelle() + "] " + motif.trim());
                }

                ControleAcces.memoriserMessage(request, "Statut mis à jour : " + statut.getLibelle() + ".");

            } else if ("commentaire".equals(action)) {
                sinistreService.ajouterCommentaire(sinistreId, agentId, request.getParameter("commentaire"));
                ControleAcces.memoriserMessage(request, "Commentaire ajouté.");

            } else if ("indemniser".equals(action)) {
                BigDecimal montant = lireMontant(request.getParameter("montant"));
                sinistreService.indemniser(sinistreId, montant);
                ControleAcces.memoriserMessage(request, "Indemnisation de " + montant + " € enregistrée.");

            } else {
                throw new IllegalArgumentException("Action inconnue.");
            }

        } catch (IllegalArgumentException e) {
            // Règle métier refusée par le service (message destiné à l'agent)
            request.getSession().setAttribute(CLE_ERREUR, e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute(CLE_ERREUR, "Une erreur est survenue. Veuillez réessayer.");
        }

        response.sendRedirect(request.getContextPath() + "/agent/sinistre?id=" + sinistreId);
    }

    private String espace(HttpServletRequest request) {
        return request.getServletPath().startsWith("/admin") ? "admin" : "agent";
    }

    private void transfererErreur(HttpServletRequest request) {
        Object erreur = request.getSession().getAttribute(CLE_ERREUR);

        if (erreur != null) {
            request.setAttribute("erreur", erreur);
            request.getSession().removeAttribute(CLE_ERREUR);
        }
    }

    private StatutSinistre lireStatut(String valeur) {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("Veuillez choisir un statut.");
        }

        try {
            return StatutSinistre.valueOf(valeur.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Statut invalide.");
        }
    }

    private BigDecimal lireMontant(String valeur) {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("Le montant d'indemnisation est obligatoire.");
        }

        try {
            return new BigDecimal(valeur.trim().replace(" ", "").replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le montant d'indemnisation doit être un nombre.");
        }
    }
}
