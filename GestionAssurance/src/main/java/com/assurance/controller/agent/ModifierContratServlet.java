package com.assurance.controller.agent;
import jakarta.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

import java.io.IOException;
import java.util.List;

import org.hibernate.Session;

import com.assurance.model.Contrat;
import com.assurance.model.Client;
import com.assurance.model.TypeAssurance;
import com.assurance.model.Garantie;
import com.assurance.service.ContratService;
import com.assurance.service.TypeAssuranceService;
import com.assurance.service.GarantieService;
import com.assurance.util.HibernateUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

@WebServlet("/agent/modifier-contrat")
public class ModifierContratServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ContratService contratService = new ContratService();
    private TypeAssuranceService typeService = new TypeAssuranceService();
    private GarantieService garantieService = new GarantieService();
    
    private boolean verifierAgent(HttpServletRequest request,HttpServletResponse response)throws IOException {
    	HttpSession session = request.getSession(false);
    	if (session == null ||!"AGENT".equalsIgnoreCase(
    		(String) session.getAttribute("role"))) {
    		response.sendError(HttpServletResponse.SC_FORBIDDEN);
    		return false;}
    	return true;
}
    
    
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
    	if (!verifierAgent(request, response)) {
    	    return;
    	}
        try {
            Long id = Long.parseLong(request.getParameter("id"));

            Contrat contrat = contratService.trouverParId(id);

            if (contrat == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            try (Session session =
                    HibernateUtil.getSessionFactory().openSession()) {

                List<Client> clients = session.createQuery(
                        "FROM Client c WHERE c.actif = true",
                        Client.class).list();

                request.setAttribute("clients", clients);
            }

            request.setAttribute("contrat", contrat);
            request.setAttribute("types", typeService.lister());
            request.setAttribute("garanties", garantieService.lister());

            Set<Long> garantiesSelectionneesIds = new HashSet<>();

            if (contrat.getGaranties() != null) {
                for (Garantie garantie : contrat.getGaranties()) {
                    garantiesSelectionneesIds.add(garantie.getId());
                }
            }

            request.setAttribute("garantiesSelectionneesIds", garantiesSelectionneesIds);
            request.getRequestDispatcher(
                    "/agent/modifier-contrat.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
    	if (!verifierAgent(request, response)) {
    	    return;
    	}
        request.setCharacterEncoding("UTF-8");

        try {
            Long id = Long.parseLong(request.getParameter("id"));

            Contrat contrat = contratService.trouverParId(id);

            if (contrat == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            contrat.setNumeroContrat(
                    request.getParameter("numeroContrat"));

            contrat.setDateDebut(
                    LocalDate.parse(request.getParameter("dateDebut")));

            contrat.setDateFin(
                    LocalDate.parse(request.getParameter("dateFin")));

            contrat.setMontant(
                    new BigDecimal(request.getParameter("montant")));

            Long clientId = Long.parseLong(
                    request.getParameter("clientId"));

            Long typeId = Long.parseLong(
                    request.getParameter("typeAssuranceId"));

            String[] garantieIds =
                    request.getParameterValues("garantieIds");

            try (Session session =
                    HibernateUtil.getSessionFactory().openSession()) {

                Client client = session.get(Client.class, clientId);
                TypeAssurance type = session.get(TypeAssurance.class, typeId);

                if (client == null || type == null) {
                    throw new IllegalArgumentException(
                            "Client ou type d'assurance introuvable.");
                }

                List<Garantie> garanties = new ArrayList<>();
                Set<Long> idsSelectionnes = new HashSet<>();

                // Récupérer les garanties sélectionnées dans le formulaire
                if (garantieIds != null) {
                    for (String garantieId : garantieIds) {
                        Long idGarantie = Long.parseLong(garantieId);

                        // Éviter les doublons
                        if (!idsSelectionnes.add(idGarantie)) {
                            continue;
                        }

                        Garantie garantie = session.get(Garantie.class, idGarantie);

                        if (garantie == null) {
                            throw new IllegalArgumentException("Garantie introuvable.");
                        }

                        garanties.add(garantie);
                    }
                }

                // Conserver les garanties désactivées déjà associées au contrat
                if (contrat.getGaranties() != null) {
                    for (Garantie ancienne : contrat.getGaranties()) {

                        if (!ancienne.isActif()
                                && !idsSelectionnes.contains(ancienne.getId())) {

                            Garantie garantie = session.get(
                                Garantie.class,
                                ancienne.getId()
                            );

                            if (garantie != null) {
                                garanties.add(garantie);
                            }
                        }
                    }
                }

                contrat.setClient(client);
                contrat.setTypeAssurance(type);
                contrat.setGaranties(garanties);
            }

            contratService.modifier(contrat);

            request.getSession().setAttribute(
                    "messageSucces",
                    "Contrat modifié avec succès !");

            response.sendRedirect(
                    request.getContextPath() + "/agent/contrats");

        } catch (IllegalArgumentException e) {

            request.getSession().setAttribute(
                    "messageErreur", e.getMessage());

            response.sendRedirect(
                    request.getContextPath() + "/agent/contrats");
        }
    }
    
}
