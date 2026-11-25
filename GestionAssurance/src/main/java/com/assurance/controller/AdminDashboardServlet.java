package com.assurance.controller;
import com.assurance.service.StatistiquesService;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("utilisateurId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equalsIgnoreCase(role)
                && !"ADMINISTRATEUR".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        StatistiquesService statistiquesService = new StatistiquesService();

        request.setAttribute("statistiques", statistiquesService.obtenirStatistiques());

        request.getRequestDispatcher("/admin/dashboard.jsp")
               .forward(request, response);
    }
}