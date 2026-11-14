package com.assurance.controller;

import java.io.IOException;

import com.assurance.service.GarantieService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/desactiver-garantie")
public class DesactiverGarantieServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private GarantieService garantieService = new GarantieService();

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));

            garantieService.desactiver(id);

            request.getSession().setAttribute(
                    "messageSucces",
                    "Garantie désactivée avec succès !");

        } catch (IllegalArgumentException e) {

            request.getSession().setAttribute(
                    "messageErreur",
                    e.getMessage());
        }

        response.sendRedirect(
                request.getContextPath() + "/admin/garanties");
    }
}