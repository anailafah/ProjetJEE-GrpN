package com.assurance.controller.agent;

import java.io.IOException;

import com.assurance.service.GarantieService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/agent/activer-garantie")
public class AgentActiverGarantieServlet extends HttpServlet {

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
            String idParam = request.getParameter("id");

            if (idParam == null || idParam.trim().isEmpty()) {
                throw new IllegalArgumentException(
                    "Identifiant de garantie manquant."
                );
            }

            Long id = Long.parseLong(idParam);

            garantieService.activer(id);

            session.setAttribute(
                "messageSucces",
                "Garantie réactivée avec succès !"
            );

        } catch (NumberFormatException e) {

            session.setAttribute(
                "messageErreur",
                "Identifiant de garantie invalide."
            );

        } catch (IllegalArgumentException e) {

            session.setAttribute(
                "messageErreur",
                "Réactivation impossible : " + e.getMessage()
            );

        } catch (Exception e) {

            session.setAttribute(
                "messageErreur",
                "Une erreur est survenue lors de la réactivation."
            );
        }

        response.sendRedirect(
            request.getContextPath() + "/agent/garanties"
        );
    }
}