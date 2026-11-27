package com.assurance.controller.client;

import java.io.IOException;

import com.assurance.service.ContratService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/client/mes-demandes")
public class MesDemandesServlet extends HttpServlet {

    private final ContratService contratService = new ContratService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                !"CLIENT".equalsIgnoreCase((String) session.getAttribute("role")) ||
                !(session.getAttribute("utilisateurId") instanceof Long)) {

            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Object utilisateurId = session.getAttribute("utilisateurId");

        if (!(utilisateurId instanceof Long)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Long clientId = (Long) utilisateurId;

        request.setAttribute(
            "contrats",
            contratService.listerParClient(clientId)
        );

        request.getRequestDispatcher("/client/mes-demandes.jsp")
               .forward(request, response);
    }
}