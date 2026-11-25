package com.assurance.controller;

import java.io.IOException;

import com.assurance.service.GarantieService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/agent/supprimer-garantie")
public class AgentSupprimerGarantieServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private GarantieService garantieService = new GarantieService();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            !"AGENT".equalsIgnoreCase(
                String.valueOf(session.getAttribute("role")))) {

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Long id = Long.parseLong(request.getParameter("id"));

            garantieService.supprimer(id);

            session.setAttribute("messageSucces",
                    "Garantie supprimée avec succès !");

        } catch (IllegalArgumentException e) {

            session.setAttribute("messageErreur",
                    "Suppression impossible : " + e.getMessage());

        } catch (Exception e) {

            session.setAttribute("messageErreur",
                    "Impossible de supprimer cette garantie. Elle est peut-être associée à un contrat.");
        }

        response.sendRedirect(
                request.getContextPath() + "/agent/garanties");
    }
}