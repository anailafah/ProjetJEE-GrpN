package com.assurance.controller;

import java.math.BigDecimal;

import com.assurance.model.TypeAssurance;

import java.io.IOException;
import java.util.List;

import com.assurance.model.TypeAssurance;
import com.assurance.service.TypeAssuranceService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/types-assurance")
public class TypeAssuranceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private TypeAssuranceService typeAssuranceService;

    @Override
    public void init() throws ServletException {
        typeAssuranceService = new TypeAssuranceService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<TypeAssurance> types = typeAssuranceService.lister();

        request.setAttribute("typesAssurance", types);

        request.getRequestDispatcher("/admin/types-assurance.jsp")
               .forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, NumberFormatException {

        request.setCharacterEncoding("UTF-8");

        try {
            // Récupération des données du formulaire
            String nom = request.getParameter("nom");
            String description = request.getParameter("description");
            int dureeMois = Integer.parseInt(request.getParameter("dureeMois"));
            BigDecimal tarifBase = new BigDecimal(request.getParameter("tarifBase"));

            // Création du type d'assurance
            TypeAssurance type = new TypeAssurance();

            type.setNom(nom);
            type.setDescription(description);
            type.setDureeMois(dureeMois);
            type.setTarifBase(tarifBase);

            // Enregistrement dans la base de données
            typeAssuranceService.ajouter(type);

            // Message de succès
            request.getSession().setAttribute(
                "messageSucces",
                "Type d'assurance ajouté avec succès !"
            );

            response.sendRedirect(request.getContextPath() + "/admin/types-assurance");

        } catch (IllegalArgumentException e) {

            request.setAttribute("messageErreur", e.getMessage());

            request.getRequestDispatcher("/admin/ajouter-type-assurance.jsp")
                   .forward(request, response);
        }
    }
}