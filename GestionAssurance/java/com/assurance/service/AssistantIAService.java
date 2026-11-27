package com.assurance.service;

import java.util.stream.Collectors;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;

public class AssistantIAService {

    private static volatile OpenAIClient client;

    private OpenAIClient obtenirClient() {

        if (client == null) {
            synchronized (AssistantIAService.class) {
                if (client == null) {
                    String apiKey = System.getenv("OPENAI_API_KEY");

                    if (apiKey == null || apiKey.isBlank()) {
                        throw new IllegalStateException(
                            "La variable OPENAI_API_KEY est introuvable."
                        );
                    }

                    client = OpenAIOkHttpClient.fromEnv();
                }
            }
        }

        return client;
    }

    public String repondre(String question) {

        if (question == null || question.isBlank()) {
            return "Veuillez saisir une question.";
        }

        try {
            ResponseCreateParams parametres = ResponseCreateParams.builder()
                .model(ChatModel.GPT_5_2)
                .instructions(
                    "Tu es un assistant virtuel spécialisé dans l'assurance. "
                    + "Tu réponds toujours en français, avec des explications "
                    + "claires, simples et courtoises. "
                    + "Tu aides les clients sur les contrats, les garanties, "
                    + "les types d'assurance, les sinistres et les paiements. "
                    + "Ne prétends jamais connaître les informations personnelles "
                    + "ou le contenu réel du contrat d'un client. "
                    + "Si une question nécessite l'accès à son dossier, invite-le "
                    + "à consulter son espace client ou à contacter son agent. "
                    + "Précise que tes réponses sont informatives et ne remplacent "
                    + "pas les conditions de son contrat."
                )
                .input(question)
                .maxOutputTokens(500)
                .build();

            Response reponse = obtenirClient()
                .responses()
                .create(parametres);

            String texte = reponse.output().stream()
                .flatMap(element -> element.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(contenu -> contenu.outputText().stream())
                .map(contenu -> contenu.text())
                .collect(Collectors.joining("\n"));

            if (texte.isBlank()) {
                return "Je n'ai pas réussi à générer une réponse. "
                     + "Veuillez reformuler votre question.";
            }

            return texte;

        } catch (Exception e) {
            e.printStackTrace();

            return "Désolé, l'assistant IA rencontre actuellement un problème. "
                 + "Veuillez réessayer dans quelques instants.";
        }
    }
}
