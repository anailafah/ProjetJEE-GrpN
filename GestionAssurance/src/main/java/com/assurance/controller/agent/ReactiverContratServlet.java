package com.assurance.controller.agent;

import java.io.IOException;

import com.assurance.service.ContratService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/agent/reactiver-contrat")
public class ReactiverContratServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ContratService contratService = new ContratService();

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
            String idParam = request.getParameter("id");

            if (idParam == null || idParam.trim().isEmpty()) {
                throw new IllegalArgumentException("Identifiant du contrat manquant.");
            }

            Long id = Long.parseLong(idParam);

            contratService.reactiver(id);

            session.setAttribute("messageSucces",
                    "Le contrat a été réactivé avec succès.");

        } catch (NumberFormatException e) {
            session.setAttribute("messageErreur", "Identifiant du contrat invalide.");

        } catch (IllegalArgumentException e) {
            session.setAttribute("messageErreur", e.getMessage());

        } catch (Exception e) {
            session.setAttribute("messageErreur",
                    "Une erreur est survenue lors de la réactivation.");
        }

        response.sendRedirect(request.getContextPath() + "/agent/contrats");
    }
}