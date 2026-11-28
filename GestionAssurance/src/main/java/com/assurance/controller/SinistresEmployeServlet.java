package com.assurance.controller;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
 * Liste de tous les sinistres pour l'agent (/agent/sinistres) et l'administrateur (/admin/sinistres).
 * Filtres : statut et recherche (numéro de sinistre, numéro de contrat, nom ou email du client).
 * La même vue sert aux deux espaces ; l'attribut "espace" ("agent" ou "admin") adapte le menu et les liens.
 */
@WebServlet({ "/agent/sinistres", "/admin/sinistres" })
public class SinistresEmployeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private SinistreService sinistreService = new SinistreService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!ControleAcces.exigerEmploye(request, response)) {
            return;
        }

        request.setAttribute("espace", request.getServletPath().startsWith("/admin") ? "admin" : "agent");
        request.setAttribute("statuts", StatutSinistre.values());

        StatutSinistre statut = lireStatut(request.getParameter("statut"));
        String recherche = request.getParameter("recherche");

        request.setAttribute("statutChoisi", statut);
        request.setAttribute("recherche", recherche);

        try {
            List<Sinistre> tous = sinistreService.lister();

            // Compteurs par statut (pour les pastilles en haut de la page)
            Map<StatutSinistre, Long> compteurs = new EnumMap<>(StatutSinistre.class);
            for (StatutSinistre s : StatutSinistre.values()) {
                compteurs.put(s, 0L);
            }
            for (Sinistre s : tous) {
                if (s.getStatut() != null) {
                    compteurs.merge(s.getStatut(), 1L, Long::sum);
                }
            }

            List<Sinistre> affiches = tous.stream()
                    .filter(s -> statut == null || statut == s.getStatut())
                    .filter(s -> correspond(s, recherche))
                    .collect(Collectors.toList());

            request.setAttribute("compteurs", compteurs);
            request.setAttribute("total", tous.size());
            request.setAttribute("sinistres", affiches);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erreur", "Impossible de charger les sinistres.");
        }

        request.getRequestDispatcher("/WEB-INF/views/sinistres/liste.jsp").forward(request, response);
    }

    private StatutSinistre lireStatut(String valeur) {
        if (valeur == null || valeur.isBlank()) {
            return null;
        }

        try {
            return StatutSinistre.valueOf(valeur.trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private boolean correspond(Sinistre s, String recherche) {
        if (recherche == null || recherche.isBlank()) {
            return true;
        }

        String motCle = recherche.trim().toLowerCase(Locale.ROOT);

        StringBuilder texte = new StringBuilder();
        texte.append(s.getNumeroSinistre()).append(' ');

        if (s.getContrat() != null) {
            texte.append(s.getContrat().getNumeroContrat()).append(' ');

            if (s.getContrat().getClient() != null) {
                texte.append(s.getContrat().getClient().getNom()).append(' ')
                     .append(s.getContrat().getClient().getPrenom()).append(' ')
                     .append(s.getContrat().getClient().getEmail());
            }
        }

        return texte.toString().toLowerCase(Locale.ROOT).contains(motCle);
    }
}
