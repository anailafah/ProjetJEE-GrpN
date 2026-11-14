package com.assurance.controller;

import java.io.IOException;

import com.assurance.service.TypeAssuranceService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/desactiver-type-assurance")
public class DesactiverTypeAssuranceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private TypeAssuranceService typeAssuranceService =
            new TypeAssuranceService();

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));

            typeAssuranceService.desactiver(id);

            request.getSession().setAttribute(
                    "messageSucces",
                    "Type d'assurance désactivé avec succès !");

        } catch (IllegalArgumentException e) {

            request.getSession().setAttribute(
                    "messageErreur",
                    e.getMessage());
        }

        response.sendRedirect(
                request.getContextPath() + "/admin/types-assurance");
    }
}
