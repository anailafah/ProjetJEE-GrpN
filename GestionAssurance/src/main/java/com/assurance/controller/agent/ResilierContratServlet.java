package com.assurance.controller.agent;

import java.io.IOException;

import com.assurance.service.ContratService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/agent/resilier-contrat")
public class ResilierContratServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ContratService contratService = new ContratService();

    private boolean verifierAgent(HttpServletRequest request,
                                  HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            !"AGENT".equalsIgnoreCase(
                (String) session.getAttribute("role"))) {

            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        if (!verifierAgent(request, response)) {
            return;
        }

        try {
            Long id = Long.parseLong(request.getParameter("id"));

            contratService.annuler(id);

            request.getSession().setAttribute(
                "messageSucces",
                "Contrat résilié avec succès."
            );

        } catch (IllegalArgumentException e) {

            request.getSession().setAttribute(
                "messageErreur",
                e.getMessage()
            );
        }

        response.sendRedirect(
            request.getContextPath() + "/agent/contrats"
        );
    }
}