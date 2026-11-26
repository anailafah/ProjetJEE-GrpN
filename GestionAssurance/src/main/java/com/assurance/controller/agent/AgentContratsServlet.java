package com.assurance.controller.agent;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.assurance.model.Contrat;
import com.assurance.service.ContratService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/agent/contrats")
public class AgentContratsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ContratService contratService = new ContratService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            !"AGENT".equalsIgnoreCase(
                String.valueOf(session.getAttribute("role")))) {

            response.sendRedirect(
                request.getContextPath() + "/login");
            return;
        }

        try {
            List<Contrat> contrats = contratService.lister();

            String recherche = request.getParameter("recherche");

            if (recherche != null && !recherche.trim().isEmpty()) {

                String motCle = recherche.trim().toLowerCase();

                contrats = contrats.stream()
                    .filter(contrat ->
                        (contrat.getNumeroContrat() != null &&
                         contrat.getNumeroContrat().toLowerCase()
                             .contains(motCle))
                        ||
                        (contrat.getClient() != null &&
                         ((contrat.getClient().getNom() != null &&
                           contrat.getClient().getNom().toLowerCase()
                               .contains(motCle))
                         ||
                          (contrat.getClient().getPrenom() != null &&
                           contrat.getClient().getPrenom().toLowerCase()
                               .contains(motCle)))
                        )
                    )
                    .collect(Collectors.toList());
            }

            request.setAttribute("contrats", contrats);
            request.setAttribute("recherche", recherche);

            request.getRequestDispatcher(
                "/agent/contrats.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            throw new ServletException(
                "Erreur lors du chargement des contrats.", e);
        }
    }
}