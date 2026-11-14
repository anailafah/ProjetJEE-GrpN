package com.assurance.controller;

import java.math.BigDecimal;

import com.assurance.model.Garantie;

import java.io.IOException;

import com.assurance.service.GarantieService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/garanties")
public class GarantieServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private GarantieService garantieService = new GarantieService();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("garanties", garantieService.lister());

        request.getRequestDispatcher("/admin/garanties.jsp")
                .forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            Garantie garantie = new Garantie();

            garantie.setNom(request.getParameter("nom"));
            garantie.setDescription(request.getParameter("description"));

            BigDecimal montant = new BigDecimal(
                    request.getParameter("montantCouverture"));

            garantie.setMontantCouverture(montant);

            garantieService.ajouter(garantie);

            request.getSession().setAttribute(
                    "messageSucces",
                    "Garantie ajoutée avec succès !");

            response.sendRedirect(
                    request.getContextPath() + "/admin/garanties");

        } catch (IllegalArgumentException e) {

            request.setAttribute("messageErreur", e.getMessage());

            request.getRequestDispatcher("/admin/ajouter-garantie.jsp")
                    .forward(request, response);
        }
    }
}
