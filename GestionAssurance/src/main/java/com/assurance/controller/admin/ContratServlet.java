package com.assurance.controller.admin;

import java.io.IOException;

import com.assurance.service.ContratService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/contrats")
public class ContratServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ContratService contratService = new ContratService();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("contrats", contratService.lister());

        request.getRequestDispatcher("/admin/contrats.jsp")
                .forward(request, response);
    }
}