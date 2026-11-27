package com.assurance.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.assurance.model.DocumentSinistre;
import com.assurance.service.SinistreService;
import com.assurance.util.StockageDocuments;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Ouverture d'une pièce justificative.
 * Une seule servlet pour les trois espaces (AuthFilter vérifie déjà le rôle selon le préfixe) :
 *  - client : uniquement les documents de ses propres sinistres
 *  - agent / admin : tous les documents
 */
@WebServlet({ "/client/document-sinistre", "/agent/document-sinistre", "/admin/document-sinistre" })
public class DocumentSinistreServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private SinistreService sinistreService = new SinistreService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("utilisateurId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long utilisateurId = ((Number) session.getAttribute("utilisateurId")).longValue();
        String role = (String) session.getAttribute("role");

        try {
            String idParam = request.getParameter("id");
            DocumentSinistre document = sinistreService.trouverDocumentParId(
                    idParam == null ? null : Long.valueOf(idParam));

            if (document == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Document introuvable.");
                return;
            }

            // Un client ne peut ouvrir que les documents de ses sinistres
            if ("CLIENT".equals(role)) {
                sinistreService.trouverPourClient(document.getSinistre().getId(), utilisateurId);
            }

            Path fichier = StockageDocuments.resoudre(document.getCheminFichier());

            if (!Files.isRegularFile(fichier)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Le fichier n'est plus disponible sur le serveur.");
                return;
            }

            String nomEncode = URLEncoder.encode(document.getNomFichier(), StandardCharsets.UTF_8)
                    .replace("+", "%20");

            response.setContentType(StockageDocuments.typeContenu(document.getNomFichier()));
            response.setContentLengthLong(Files.size(fichier));
            response.setHeader("Content-Disposition", "inline; filename*=UTF-8''" + nomEncode);
            response.setHeader("X-Content-Type-Options", "nosniff");

            Files.copy(fichier, response.getOutputStream());

        } catch (IllegalArgumentException e) {
            // id mal formé, document d'un autre client ou chemin invalide
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Document introuvable.");
        }
    }
}
