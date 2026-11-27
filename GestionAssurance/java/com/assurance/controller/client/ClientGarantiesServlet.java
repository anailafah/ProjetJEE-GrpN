package com.assurance.controller.client;

import java.io.IOException;

import com.assurance.service.ContratService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/client/garanties")
public class ClientGarantiesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ContratService contratService = new ContratService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("utilisateurId") == null
                || !"CLIENT".equals(session.getAttribute("role"))) {

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long clientId = ((Number) session.getAttribute("utilisateurId")).longValue();

        try {
            request.setAttribute("contrats",
                    contratService.listerParClient(clientId));

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erreur",
                    "Impossible de charger vos garanties.");
        }

        request.getRequestDispatcher("/client/garanties.jsp")
               .forward(request, response);
    }
}