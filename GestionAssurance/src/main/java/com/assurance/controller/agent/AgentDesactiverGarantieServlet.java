package com.assurance.controller.agent;

import java.io.IOException;

import com.assurance.service.GarantieService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/agent/desactiver-garantie")
public class AgentDesactiverGarantieServlet extends HttpServlet {

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

            garantieService.desactiver(id);

            session.setAttribute("messageSucces",
                    "Garantie désactivée avec succès !");

        } catch (IllegalArgumentException e) {

            session.setAttribute("messageErreur",
                    "Désactivation impossible : " + e.getMessage());

        } catch (Exception e) {

            session.setAttribute("messageErreur",
                    "Une erreur est survenue lors de la désactivation.");
        }

        response.sendRedirect(
                request.getContextPath() + "/agent/garanties");
    }
}