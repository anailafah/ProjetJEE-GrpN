package com.assurance.controller;

import java.io.IOException;

import com.assurance.model.TypeAssurance;
import com.assurance.service.TypeAssuranceService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/modifier-type-assurance")
public class ModifierTypeAssuranceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private TypeAssuranceService typeAssuranceService =
            new TypeAssuranceService();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));

            TypeAssurance type = typeAssuranceService.trouverParId(id);

            if (type == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Type d'assurance introuvable.");
                return;
            }

            request.setAttribute("type", type);

            request.getRequestDispatcher(
                    "/admin/modifier-type-assurance.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Identifiant invalide.");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            Long id = Long.parseLong(request.getParameter("id"));

            String nom = request.getParameter("nom");
            String description = request.getParameter("description");

            int dureeMois = Integer.parseInt(
                    request.getParameter("dureeMois"));

            java.math.BigDecimal tarifBase =
                    new java.math.BigDecimal(
                            request.getParameter("tarifBase"));

            TypeAssurance type = typeAssuranceService.trouverParId(id);

            if (type == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Type d'assurance introuvable.");
                return;
            }

            type.setNom(nom);
            type.setDescription(description);
            type.setDureeMois(dureeMois);
            type.setTarifBase(tarifBase);

            typeAssuranceService.modifier(type);

            request.getSession().setAttribute(
                    "messageSucces",
                    "Type d'assurance modifié avec succès !");

            response.sendRedirect(
                    request.getContextPath() + "/admin/types-assurance");

        } catch (IllegalArgumentException e) {

            request.setAttribute("messageErreur", e.getMessage());

            request.getRequestDispatcher(
                    "/admin/types-assurance").forward(request, response);
        }
    }
}
