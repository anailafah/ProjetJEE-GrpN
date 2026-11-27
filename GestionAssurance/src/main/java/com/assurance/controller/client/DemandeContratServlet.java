package com.assurance.controller.client;
import com.assurance.model.TypeAssurance;
import com.assurance.model.Garantie;

import java.io.IOException;

import com.assurance.dao.TypeAssuranceDAO;
import com.assurance.dao.GarantieDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/client/demande-contrat")
public class DemandeContratServlet extends HttpServlet {

    private TypeAssuranceDAO typeAssuranceDAO = new TypeAssuranceDAO();
    private GarantieDAO garantieDAO = new GarantieDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"CLIENT".equalsIgnoreCase(
                (String) session.getAttribute("role"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("typesAssurance",
        	    typeAssuranceDAO.lister().stream()
        	        .filter(TypeAssurance::isActif)
        	        .toList()
        	);

        	request.setAttribute("garanties",
        	    garantieDAO.lister().stream()
        	        .filter(Garantie::isActif)
        	        .toList()
        	);
        request.getRequestDispatcher("/client/demande-contrat.jsp")
               .forward(request, response);
    }
}