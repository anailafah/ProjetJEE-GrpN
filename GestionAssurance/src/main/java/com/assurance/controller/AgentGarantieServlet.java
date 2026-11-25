package com.assurance.controller;

import java.io.IOException;

import com.assurance.service.GarantieService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/agent/garanties")
public class AgentGarantieServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private GarantieService garantieService = new GarantieService();

    @Override
    protected void doGet(HttpServletRequest request,
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
            request.setAttribute("garanties", garantieService.lister());

            request.getRequestDispatcher("/agent/garanties.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            throw new ServletException(
                "Erreur lors du chargement des garanties.", e);
        }
    }
}