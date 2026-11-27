package com.assurance.controller.admin;

import java.io.IOException;

import com.assurance.service.TypeAssuranceService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/activer-type-assurance")
public class ActiverTypeAssuranceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private TypeAssuranceService typeAssuranceService;

    @Override
    public void init() throws ServletException {
        typeAssuranceService = new TypeAssuranceService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Long id = Long.parseLong(request.getParameter("id"));

            typeAssuranceService.activer(id);

            session.setAttribute(
                "messageSucces",
                "Type d'assurance réactivé avec succès !"
            );

        } catch (IllegalArgumentException e) {

            session.setAttribute(
                "messageErreur",
                e.getMessage()
            );
        }

        response.sendRedirect(
            request.getContextPath() + "/admin/types-assurance"
        );
    }
}