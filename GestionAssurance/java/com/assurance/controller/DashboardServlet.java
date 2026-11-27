package com.assurance.controller;

import java.io.IOException;

import com.assurance.model.Statistiques;
import com.assurance.service.StatistiquesService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private StatistiquesService statistiquesService = new StatistiquesService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Statistiques stats = statistiquesService.obtenirStatistiques();

        request.setAttribute("stats", stats);

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp")
               .forward(request, response);
    }
}