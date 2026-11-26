package com.assurance.controller.agent;

import java.io.IOException;

import com.assurance.service.ContratService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/agent/suspendre-contrat")
public class SuspendreContratServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ContratService contratService = new ContratService();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            !"AGENT".equalsIgnoreCase(
                String.valueOf(session.getAttribute("role")))) {

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Long id = Long.parseLong(request.getParameter("id"));

            contratService.suspendre(id);

            session.setAttribute(
                "messageSucces",
                "Le contrat a été suspendu avec succès."
            );

        } catch (Exception e) {

            session.setAttribute(
                "messageErreur",
                e.getMessage()
            );
        }

        response.sendRedirect(
            request.getContextPath() + "/agent/contrats"
        );
    }
}