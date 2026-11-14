package com.assurance.controller;

import java.io.IOException;

import com.assurance.service.ContratService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/annuler-contrat")
public class AnnulerContratServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ContratService contratService = new ContratService();

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));

            contratService.annuler(id);

            request.getSession().setAttribute(
                "messageSucces",
                "Contrat annulé avec succès !"
            );

        } catch (IllegalArgumentException e) {

            request.getSession().setAttribute(
                "messageErreur",
                e.getMessage()
            );
        }

        response.sendRedirect(
            request.getContextPath() + "/admin/contrats"
        );
    }
}