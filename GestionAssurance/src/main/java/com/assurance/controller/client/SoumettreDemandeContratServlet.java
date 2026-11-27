package com.assurance.controller.client;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;

import com.assurance.dao.GarantieDAO;
import com.assurance.dao.TypeAssuranceDAO;
import com.assurance.model.Client;
import com.assurance.model.Contrat;
import com.assurance.model.Garantie;
import com.assurance.model.TypeAssurance;
import com.assurance.service.ContratService;
import com.assurance.util.HibernateUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/client/soumettre-demande")
public class SoumettreDemandeContratServlet extends HttpServlet {

    private final ContratService contratService = new ContratService();
    private final TypeAssuranceDAO typeAssuranceDAO = new TypeAssuranceDAO();
    private final GarantieDAO garantieDAO = new GarantieDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null ||
                !"CLIENT".equalsIgnoreCase((String) session.getAttribute("role"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            // Récupération du client connecté
        	Object utilisateurId = session.getAttribute("utilisateurId");

        	if (!(utilisateurId instanceof Long)) {
        	    throw new IllegalArgumentException("Session client invalide.");
        	}

        	Long clientId = (Long) utilisateurId;
        	

            Client client;

            try (Session hibernateSession =
                    HibernateUtil.getSessionFactory().openSession()) {

                client = hibernateSession.get(Client.class, clientId);
            }

            if (client == null) {
                throw new IllegalArgumentException("Client introuvable.");
            }

            // Récupération du type d'assurance
            Long typeId = Long.parseLong(
                request.getParameter("typeAssuranceId")
            );

            TypeAssurance type = typeAssuranceDAO.trouverParId(typeId);

            if (type == null || !type.isActif()) {
                throw new IllegalArgumentException(
                    "Type d'assurance indisponible."
                );
            }

            // Récupération des garanties sélectionnées
            String[] garantiesIds = request.getParameterValues("garanties");

            if (garantiesIds == null || garantiesIds.length == 0) {
                throw new IllegalArgumentException(
                    "Veuillez sélectionner au moins une garantie."
                );
            }

            List<Garantie> garanties = new ArrayList<>();

            for (String id : garantiesIds) {

                Long garantieId = Long.parseLong(id);

                Garantie garantie = garantieDAO.trouverParId(garantieId);

                if (garantie == null || !garantie.isActif()) {
                    throw new IllegalArgumentException(
                        "Une garantie sélectionnée est indisponible."
                    );
                }

                garanties.add(garantie);
            }

            // Récupération des dates et du montant
            LocalDate dateDebut = LocalDate.parse(
                request.getParameter("dateDebut")
            );

            LocalDate dateFin = LocalDate.parse(
                request.getParameter("dateFin")
            );

            BigDecimal montant = new BigDecimal(
                request.getParameter("montant")
            );

            // Construction de la demande
            Contrat contrat = new Contrat();

            String numero = "DEM-" +
                UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0, 12)
                    .toUpperCase();

            contrat.setNumeroContrat(numero);
            contrat.setClient(client);
            contrat.setTypeAssurance(type);
            contrat.setGaranties(garanties);
            contrat.setDateDebut(dateDebut);
            contrat.setDateFin(dateFin);
            contrat.setMontant(montant);

            // Enregistrement avec le statut EN_ATTENTE
            contratService.demanderContrat(contrat);

            session.setAttribute(
                "messageSucces",
                "Votre demande de contrat a été envoyée avec succès."
            );

        } catch (IllegalArgumentException e) {

            session.setAttribute("messageErreur", e.getMessage());

        } catch (RuntimeException e) {

            getServletContext().log(
                "Erreur lors de la soumission de la demande de contrat", e
            );

            session.setAttribute(
                "messageErreur",
                "Une erreur est survenue lors de l'envoi de votre demande."
            );
        }

        response.sendRedirect(
            request.getContextPath() + "/client/demande-contrat"
        );
    }
}