package com.assurance.controller;

import java.io.IOException;

import com.assurance.service.AssistantIAService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/assistant-ia")
public class AssistantIAServlet extends HttpServlet {

    private final AssistantIAService assistantIAService = new AssistantIAService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"CLIENT".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.getRequestDispatcher("/client/assistant-ia.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || !"CLIENT".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String question = request.getParameter("question");

        if (question == null || question.isBlank()) {
            request.setAttribute("erreur", "Veuillez saisir une question.");
        } else {
            String reponse = assistantIAService.repondre(question);
            request.setAttribute("question", question);
            request.setAttribute("reponse", reponse);
        }

        request.getRequestDispatcher("/client/assistant-ia.jsp")
               .forward(request, response);
    }
}