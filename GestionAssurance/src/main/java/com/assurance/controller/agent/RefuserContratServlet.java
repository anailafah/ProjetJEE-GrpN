package com.assurance.controller.agent;

import java.io.IOException;

import com.assurance.service.ContratService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/agent/refuser-contrat")
public class RefuserContratServlet extends HttpServlet {

    private ContratService contratService = new ContratService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"AGENT".equalsIgnoreCase((String) session.getAttribute("role"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Long id = Long.parseLong(request.getParameter("id"));

            contratService.refuser(id);

            session.setAttribute("messageSucces", "Demande de contrat refusée avec succès.");

        } catch (IllegalArgumentException e) {
            session.setAttribute("messageErreur", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/agent/contrats");
    }
}