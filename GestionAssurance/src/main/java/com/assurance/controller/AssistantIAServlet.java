package com.assurance.controller;

import java.io.IOException;

import com.assurance.service.AssistantIAService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/assistant-ia")
public class AssistantIAServlet extends HttpServlet {

    private AssistantIAService assistantIAService = new AssistantIAService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/assistant-ia.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String question = request.getParameter("question");

        String reponse = assistantIAService.repondre(question);

        request.setAttribute("question", question);
        request.setAttribute("reponse", reponse);

        request.getRequestDispatcher("/WEB-INF/views/assistant-ia.jsp")
               .forward(request, response);
    }
}