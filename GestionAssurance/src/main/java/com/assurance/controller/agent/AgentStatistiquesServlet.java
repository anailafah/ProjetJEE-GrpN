package com.assurance.controller.agent;

import java.io.IOException;

import com.assurance.service.StatistiquesService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Page des statistiques de l'agent.
 * L'accès (connexion + rôle AGENT) est contrôlé par AuthFilter sur /agent/*.
 */
@WebServlet("/agent/statistiques")
public class AgentStatistiquesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final StatistiquesService statistiquesService = new StatistiquesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("statistiques", statistiquesService.obtenirStatistiques());

        request.getRequestDispatcher("/agent/statistiques.jsp")
               .forward(request, response);
    }
}
