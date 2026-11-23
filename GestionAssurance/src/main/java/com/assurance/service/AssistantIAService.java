package com.assurance.service;

public class AssistantIAService {

    public String repondre(String question) {

        if (question == null || question.trim().isEmpty()) {
            return "Veuillez saisir une question.";
        }

        String q = question.toLowerCase();

        if (q.contains("contrat")) {
            return "Un contrat d'assurance est un accord entre l'assureur et le client qui définit les garanties et les conditions de couverture.";
        }

        if (q.contains("sinistre")) {
            return "Un sinistre est un événement qui peut donner lieu à une indemnisation selon les garanties du contrat.";
        }

        if (q.contains("paiement") || q.contains("payer")) {
            return "Vous pouvez consulter les paiements et les échéances dans la rubrique Paiements.";
        }

        if (q.contains("garantie")) {
            return "Les garanties précisent les risques couverts et les montants maximums pris en charge par l'assurance.";
        }

        if (q.contains("bonjour") || q.contains("salut")) {
            return "Bonjour ! Je suis votre assistant virtuel. Comment puis-je vous aider ?";
        }

        return "Je n'ai pas compris votre question. Essayez de demander des informations sur les contrats, les sinistres, les paiements ou les garanties.";
    }
}