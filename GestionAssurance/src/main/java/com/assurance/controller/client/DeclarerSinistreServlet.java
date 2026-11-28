package com.assurance.controller.client;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import com.assurance.model.Contrat;
import com.assurance.model.Sinistre;
import com.assurance.model.TypeSinistre;
import com.assurance.service.ContratService;
import com.assurance.service.SinistreService;
import com.assurance.util.ControleAcces;
import com.assurance.util.StockageDocuments;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * Déclaration d'un sinistre par le client, avec pièces justificatives.
 * GET  : affiche le formulaire (contrats actifs du client + types de sinistre)
 * POST : vérifie les données, enregistre le sinistre puis les fichiers
 */
@WebServlet("/client/declarer-sinistre")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,       // au-delà de 1 Mo, fichier temporaire sur disque
    maxFileSize = 6L * 1024 * 1024,        // garde-fou serveur (la vraie limite de 5 Mo est vérifiée plus bas)
    maxRequestSize = 32L * 1024 * 1024
)
public class DeclarerSinistreServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String VUE = "/client/declarer-sinistre.jsp";

    private SinistreService sinistreService = new SinistreService();
    private ContratService contratService = new ContratService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long clientId = ControleAcces.exigerClient(request, response);
        if (clientId == null) {
            return;
        }

        afficherFormulaire(request, response, clientId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Long clientId = ControleAcces.exigerClient(request, response);
        if (clientId == null) {
            return;
        }

        try {
            // 1. Vérification des fichiers avant de créer quoi que ce soit
            List<Part> fichiers = StockageDocuments.lireFichiers(request, "pieces");

            // 2. Lecture et conversion des champs du formulaire
            Sinistre sinistre = new Sinistre();
            sinistre.setTypeSinistre(lireType(request.getParameter("typeSinistre")));
            sinistre.setDateSinistre(lireDate(request.getParameter("dateSinistre")));
            sinistre.setDescription(request.getParameter("description"));
            sinistre.setMontantDemande(lireMontant(request.getParameter("montantDemande")));

            Long contratId = ControleAcces.lireId(request.getParameter("contratId"));

            // 3. Règles métier (contrat du client, contrat actif, dates, montant...)
            Sinistre enregistre = sinistreService.declarer(sinistre, contratId, clientId);

            // 4. Pièces justificatives
            int erreursFichiers = 0;

            for (Part fichier : fichiers) {
                try {
                    String chemin = StockageDocuments.enregistrer(enregistre.getId(), fichier);
                    sinistreService.ajouterDocument(enregistre.getId(),
                            StockageDocuments.nomOriginal(fichier), chemin);
                } catch (Exception e) {
                    e.printStackTrace();
                    erreursFichiers++;
                }
            }

            String message = "Votre sinistre n° " + enregistre.getNumeroSinistre()
                    + " a bien été déclaré.";

            if (erreursFichiers > 0) {
                message += " Attention : " + erreursFichiers
                        + " fichier(s) n'ont pas pu être enregistrés.";
            }

            ControleAcces.memoriserMessage(request, message);

            response.sendRedirect(request.getContextPath()
                    + "/client/sinistre?id=" + enregistre.getId());

        } catch (IllegalArgumentException e) {
            request.setAttribute("erreur", e.getMessage());
            afficherFormulaire(request, response, clientId);

        } catch (IllegalStateException e) {
            // Requête trop volumineuse (limites de @MultipartConfig)
            request.setAttribute("erreur", "Les fichiers envoyés sont trop volumineux (5 Mo maximum par fichier).");
            afficherFormulaire(request, response, clientId);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erreur", "Une erreur est survenue lors de la déclaration. Veuillez réessayer.");
            afficherFormulaire(request, response, clientId);
        }
    }

    private void afficherFormulaire(HttpServletRequest request, HttpServletResponse response,
            Long clientId) throws ServletException, IOException {

        try {
            List<Contrat> contratsActifs = contratService.listerParClient(clientId)
                    .stream()
                    .filter(c -> "ACTIF".equalsIgnoreCase(c.getStatut()))
                    .collect(Collectors.toList());

            request.setAttribute("contrats", contratsActifs);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erreur", "Impossible de charger vos contrats.");
        }

        request.setAttribute("types", TypeSinistre.values());
        request.setAttribute("aujourdhui", LocalDate.now());

        request.getRequestDispatcher(VUE).forward(request, response);
    }

    private TypeSinistre lireType(String valeur) {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("Le type de sinistre est obligatoire.");
        }

        try {
            return TypeSinistre.valueOf(valeur.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Type de sinistre invalide.");
        }
    }

    private LocalDate lireDate(String valeur) {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("La date du sinistre est obligatoire.");
        }

        try {
            return LocalDate.parse(valeur.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Date du sinistre invalide.");
        }
    }

    private BigDecimal lireMontant(String valeur) {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("Le montant estimé est obligatoire.");
        }

        try {
            return new BigDecimal(valeur.trim().replace(" ", "").replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le montant estimé doit être un nombre.");
        }
    }
}
