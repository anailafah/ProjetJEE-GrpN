package com.assurance.controller;

import java.io.IOException;
import java.math.BigDecimal;

import com.assurance.model.Garantie;
import com.assurance.service.GarantieService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/modifier-garantie")
public class ModifierGarantieServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private GarantieService garantieService = new GarantieService();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));

            Garantie garantie = garantieService.trouverParId(id);

            if (garantie == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            request.setAttribute("garantie", garantie);

            request.getRequestDispatcher("/admin/modifier-garantie.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            Long id = Long.parseLong(request.getParameter("id"));

            Garantie garantie = garantieService.trouverParId(id);

            if (garantie == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            garantie.setNom(request.getParameter("nom"));
            garantie.setDescription(request.getParameter("description"));

            BigDecimal montant = new BigDecimal(
                    request.getParameter("montantCouverture"));

            garantie.setMontantCouverture(montant);

            garantieService.modifier(garantie);

            request.getSession().setAttribute(
                    "messageSucces",
                    "Garantie modifiée avec succès !");

            response.sendRedirect(
                    request.getContextPath() + "/admin/garanties");

        } catch (IllegalArgumentException e) {
            request.setAttribute("messageErreur", e.getMessage());
            request.getRequestDispatcher("/admin/garanties.jsp")
                    .forward(request, response);
        }
    }
}