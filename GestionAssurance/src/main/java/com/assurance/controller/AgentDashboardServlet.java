package com.assurance.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.assurance.util.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/agent/dashboard")
public class AgentDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("utilisateurId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");

        if (!"AGENT".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            request.setAttribute("nombreClients",
                compter(conn, "SELECT COUNT(*) FROM utilisateurs WHERE type_utilisateur = 'CLIENT'"));

            request.setAttribute("nombreContrats",
                compter(conn, "SELECT COUNT(*) FROM contrats"));

            request.setAttribute("nombreSinistres",
                compter(conn, "SELECT COUNT(*) FROM sinistres WHERE statut IN ('DECLARE', 'EN_ANALYSE')"));

            request.setAttribute("nombrePaiements",
                compter(conn, "SELECT COUNT(*) FROM paiements"));

        } catch (Exception e) {
            throw new ServletException("Erreur lors du chargement du dashboard Agent", e);
        }

        request.getRequestDispatcher("/agent/dashboard.jsp").forward(request, response);
    }

    private int compter(Connection conn, String sql) throws Exception {

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }
}