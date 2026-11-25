package com.assurance.controller;

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

@WebServlet("/agent/modifier-garantie")
public class AgentModifierGarantieServlet extends HttpServlet {

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

        try {
            Long id = Long.parseLong(request.getParameter("id"));

            Garantie garantie = garantieService.trouverParId(id);

            if (garantie == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            request.setAttribute("garantie", garantie);

            request.getRequestDispatcher("/agent/modifier-garantie.jsp")
                   .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
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
            Long id = Long.parseLong(request.getParameter("id"));

            Garantie garantie = garantieService.trouverParId(id);

            if (garantie == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            garantie.setNom(request.getParameter("nom"));
            garantie.setDescription(request.getParameter("description"));

            BigDecimal montant = new BigDecimal(
                    request.getParameter("montantCouverture"));

            garantie.setMontantCouverture(montant);

            garantieService.modifier(garantie);

            session.setAttribute("messageSucces",
                    "Garantie modifiée avec succès !");

            response.sendRedirect(
                    request.getContextPath() + "/agent/garanties");

        } catch (IllegalArgumentException e) {

            session.setAttribute("messageErreur",
                    "Modification impossible : vérifiez les informations saisies.");

            response.sendRedirect(
                    request.getContextPath() + "/agent/garanties");
        }
    }
}