package com.assurance.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.assurance.util.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/agent/clients")
public class AgentClientServlet extends HttpServlet {

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

        List<java.util.Map<String, Object>> clients = new ArrayList<>();

        String recherche = request.getParameter("recherche");

        String sql = "SELECT id, nom, prenom, email, telephone, adresse "
                   + "FROM utilisateurs WHERE type_utilisateur = 'CLIENT'";

        if (recherche != null && !recherche.trim().isEmpty()) {
            sql += " AND (nom LIKE ? OR prenom LIKE ? OR email LIKE ?)";
        }

        sql += " ORDER BY nom, prenom";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (recherche != null && !recherche.trim().isEmpty()) {
                String valeur = "%" + recherche.trim() + "%";
                ps.setString(1, valeur);
                ps.setString(2, valeur);
                ps.setString(3, valeur);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    java.util.Map<String, Object> client = new java.util.HashMap<>();

                    client.put("id", rs.getLong("id"));
                    client.put("nom", rs.getString("nom"));
                    client.put("prenom", rs.getString("prenom"));
                    client.put("email", rs.getString("email"));
                    client.put("telephone", rs.getString("telephone"));
                    client.put("adresse", rs.getString("adresse"));

                    clients.add(client);
                }
            }

        } catch (Exception e) {
            throw new ServletException("Erreur lors du chargement des clients", e);
        }

        request.setAttribute("clients", clients);
        request.getRequestDispatcher("/agent/clients.jsp").forward(request, response);
    }
}