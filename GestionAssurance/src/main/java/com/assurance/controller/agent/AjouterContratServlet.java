package com.assurance.controller.agent;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.assurance.model.Client;
import com.assurance.model.TypeAssurance;
import com.assurance.model.Garantie;
import com.assurance.model.Contrat;
import com.assurance.service.TypeAssuranceService;
import com.assurance.service.GarantieService;
import com.assurance.service.ContratService;
import com.assurance.util.HibernateUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/agent/ajouter-contrat")
public class AjouterContratServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private TypeAssuranceService typeService = new TypeAssuranceService();
    private GarantieService garantieService = new GarantieService();
    private ContratService contratService = new ContratService();

    private boolean verifierAgent(HttpServletRequest request,
                                  HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                !"AGENT".equalsIgnoreCase((String) session.getAttribute("role"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Accès réservé aux agents.");
            return false;
        }

        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (!verifierAgent(request, response)) {
            return;
        }

        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {

            List<Client> clients = session.createQuery(
                    "FROM Client c WHERE c.actif = true",
                    Client.class).list();

            List<TypeAssurance> types = typeService.lister();
            List<Garantie> garanties = garantieService.lister();

            request.setAttribute("clients", clients);
            request.setAttribute("types", types);
            request.setAttribute("garanties", garanties);

            request.getRequestDispatcher(
                    "/agent/ajouter-contrat.jsp").forward(request, response);
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
            Contrat contrat = new Contrat();

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

                if (garantieIds != null) {
                    for (String id : garantieIds) {
                        Garantie garantie = session.get(
                                Garantie.class, Long.parseLong(id));

                        if (garantie == null) {
                            throw new IllegalArgumentException(
                                    "Garantie introuvable.");
                        }

                        garanties.add(garantie);
                    }
                }

                contrat.setClient(client);
                contrat.setTypeAssurance(type);
                contrat.setGaranties(garanties);
            }

            contratService.ajouter(contrat);

            request.getSession().setAttribute(
                    "messageSucces", "Contrat créé avec succès !");

            response.sendRedirect(
                    request.getContextPath() + "/agent/contrats");

        } catch (IllegalArgumentException | DateTimeException e) {

            request.getSession().setAttribute(
                    "messageErreur", e.getMessage());

            response.sendRedirect(
                    request.getContextPath() + "/agent/ajouter-contrat");
        }
    }
}