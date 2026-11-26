package com.assurance.controller.agent;

import java.io.IOException;
import java.math.BigDecimal;

import com.assurance.model.Garantie;
import com.assurance.service.GarantieService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/agent/ajouter-garantie")
public class AgentAjouterGarantieServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private GarantieService garantieService = new GarantieService();

    private boolean verifierAgent(HttpServletRequest request,
                                  HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            !"AGENT".equalsIgnoreCase(
                String.valueOf(session.getAttribute("role")))) {

            response.sendRedirect(request.getContextPath() + "/login");
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

        request.getRequestDispatcher("/agent/ajouter-garantie.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!verifierAgent(request, response)) {
            return;
        }

        HttpSession session = request.getSession();

        try {
            String nom = request.getParameter("nom");
            String description = request.getParameter("description");
            String montantTexte = request.getParameter("montantCouverture");

            BigDecimal montant = new BigDecimal(montantTexte);

            Garantie garantie = new Garantie(
                nom,
                description,
                montant
            );

            garantieService.ajouter(garantie);

            session.setAttribute("messageSucces",
                    "La garantie a été ajoutée avec succès.");

        } catch (IllegalArgumentException e) {

            session.setAttribute("messageErreur",
                    "Données invalides. Vérifiez le montant et les informations saisies.");
        }

        response.sendRedirect(request.getContextPath() + "/agent/garanties");
    }
}