package com.assurance.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.assurance.model.Message;
import com.assurance.service.AssistantIAService;
import com.assurance.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Assistant IA du client.
 *  GET  : affiche la conversation en cours.
 *  POST : envoie une question (paramètre "question")
 *         ou démarre une nouvelle conversation (paramètre action=reinitialiser).
 * La conversation est conservée dans la session.
 */
@WebServlet("/assistant-ia")
public class AssistantIAServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String ATTRIBUT_CONVERSATION = "assistantConversation";
    private static final int LONGUEUR_MAX_QUESTION = 1000;

    /** Nombre maximal de messages conservés en session (questions + réponses). */
    private static final int TAILLE_MAX_CONVERSATION = 30;

    private final AssistantIAService assistantIAService = new AssistantIAService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = sessionClient(request, response);
        if (session == null) {
            return;
        }

        afficher(request, response, session);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = sessionClient(request, response);
        if (session == null) {
            return;
        }

        if ("reinitialiser".equals(request.getParameter("action"))) {
            session.removeAttribute(ATTRIBUT_CONVERSATION);
            response.sendRedirect(request.getContextPath() + "/assistant-ia");
            return;
        }

        String question = request.getParameter("question");
        question = (question == null) ? "" : question.trim();

        // Vérification des données saisies
        if (question.isEmpty()) {
            request.setAttribute("erreur", "Veuillez saisir une question.");
            afficher(request, response, session);
            return;
        }
        if (question.length() > LONGUEUR_MAX_QUESTION) {
            request.setAttribute("erreur",
                "Votre question est trop longue (" + LONGUEUR_MAX_QUESTION + " caractères maximum).");
            request.setAttribute("question", question);
            afficher(request, response, session);
            return;
        }

        List<Message> conversation = conversation(session);
        Long clientId = (Long) session.getAttribute("utilisateurId");
        String nomClient = session.getAttribute("prenom") + " " + session.getAttribute("nom");

        try {
            String reponse = assistantIAService.repondre(clientId, nomClient, conversation, question);

            conversation.add(new Message(Message.Auteur.CLIENT, question));
            conversation.add(new Message(Message.Auteur.ASSISTANT, reponse));
            while (conversation.size() > TAILLE_MAX_CONVERSATION) {
                conversation.remove(0);
            }
            // Réaffecter l'attribut signale la modification au conteneur (sessions répliquées)
            session.setAttribute(ATTRIBUT_CONVERSATION, conversation);

        } catch (ServiceException e) {
            request.setAttribute("erreur", e.getMessage());
            request.setAttribute("question", question);
            afficher(request, response, session);
            return;
        }

        // Post/Redirect/Get : un rafraîchissement de la page ne renvoie pas la question
        response.sendRedirect(request.getContextPath() + "/assistant-ia");
    }

    private void afficher(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        request.setAttribute("conversation", conversation(session));
        request.getRequestDispatcher("/client/assistant-ia.jsp").forward(request, response);
    }

    /** Renvoie la session si l'utilisateur est un client connecté, sinon redirige et renvoie null. */
    private HttpSession sessionClient(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("utilisateurId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        if (!"CLIENT".equals(session.getAttribute("role"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Assistant réservé aux clients.");
            return null;
        }
        return session;
    }

    @SuppressWarnings("unchecked")
    private List<Message> conversation(HttpSession session) {
        List<Message> conversation = (List<Message>) session.getAttribute(ATTRIBUT_CONVERSATION);
        if (conversation == null) {
            conversation = new ArrayList<>();
            session.setAttribute(ATTRIBUT_CONVERSATION, conversation);
        }
        return conversation;
    }
}
