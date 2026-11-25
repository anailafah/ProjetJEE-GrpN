package com.assurance.service;

import java.text.Normalizer;
import java.util.Locale;

public class AssistantIAService {

    public String repondre(String question) {

        if (question == null || question.trim().isEmpty()) {
            return "Veuillez saisir une question pour que je puisse vous aider.";
        }

        // Normalisation de la question : minuscules et suppression des accents
        String q = Normalizer.normalize(
                question.toLowerCase(Locale.ROOT),
                Normalizer.Form.NFD
        ).replaceAll("\\p{M}", "");

        // SALUTATIONS
        if (contient(q, "bonjour", "salut", "bonsoir", "coucou")) {
            return "Bonjour ! Bienvenue sur votre espace d'assistance. "
                    + "Je suis votre assistant virtuel spécialisé dans l'assurance. "
                    + "Comment puis-je vous aider aujourd'hui ?";
        }

        if (contient(q, "merci", "je te remercie")) {
            return "Avec plaisir ! Je reste à votre disposition si vous avez d'autres questions.";
        }

        if (contient(q, "au revoir", "a bientot", "bonne journee")) {
            return "Au revoir ! Merci d'avoir utilisé notre assistant. À bientôt !";
        }

        // TYPES D'ASSURANCE
        if (contient(q, "types assurance", "types d'assurance",
                "type assurance", "assurances proposees", "assurances disponibles")) {
            return "Notre plateforme propose plusieurs types d'assurance : "
                    + "automobile, habitation, santé, voyage et professionnelle. "
                    + "Chaque assurance possède ses propres garanties et conditions.";
        }

        // ASSURANCE AUTOMOBILE
        if (contient(q, "assurance auto", "assurance automobile", "voiture")) {
            return "L'assurance automobile permet de couvrir certains risques liés "
                    + "à l'utilisation d'un véhicule : responsabilité civile, accident, "
                    + "vol ou assistance, selon les garanties souscrites.";
        }

        // ASSURANCE HABITATION
        if (contient(q, "assurance habitation", "maison", "logement", "appartement")) {
            return "L'assurance habitation peut couvrir votre logement et vos biens "
                    + "contre différents risques comme l'incendie, le dégât des eaux "
                    + "ou le vol, selon les garanties de votre contrat.";
        }

        // SOUSCRIPTION
        if (contient(q, "souscrire", "souscription", "ouvrir un contrat",
                "nouveau contrat", "adhesion", "comment assurer")) {
            return "Pour souscrire une assurance, commencez par choisir le type "
                    + "d'assurance souhaité. Consultez ensuite les garanties et les "
                    + "conditions proposées. Un agent pourra vous accompagner dans "
                    + "la création de votre contrat.";
        }

        // DEFINITIONS
        if (contient(q, "qu'est-ce qu'une assurance", "c'est quoi une assurance",
                "definition assurance")) {
            return "Une assurance est un contrat par lequel un assureur s'engage "
                    + "à couvrir certains risques en échange du paiement d'une cotisation.";
        }

        if (contient(q, "franchise")) {
            return "La franchise est la somme qui reste à la charge de l'assuré "
                    + "après un sinistre. Son montant et ses conditions dépendent du contrat.";
        }

        if (contient(q, "prime", "cotisation")) {
            return "La prime ou cotisation est le montant que l'assuré doit payer "
                    + "pour bénéficier des garanties prévues dans son contrat.";
        }

        if (contient(q, "echeance", "date limite de paiement")) {
            return "Une échéance correspond à une date à laquelle un paiement "
                    + "doit être effectué. Vous pouvez consulter vos échéances "
                    + "dans la rubrique Paiements.";
        }

        // GARANTIES
        if (contient(q, "garantie", "couverture", "risques couverts")) {
            return "Les garanties définissent les risques pris en charge par votre "
                    + "assurance. Elles peuvent notamment concerner la responsabilité "
                    + "civile, le vol, l'incendie, les accidents et les dégâts des eaux. "
                    + "La couverture exacte dépend de votre contrat.";
        }

        // ACCIDENT
        if (contient(q, "accident", "collision", "accrochage")) {
            return "En cas d'accident :\n"
                    + "1. Assurez-vous que les personnes impliquées sont en sécurité.\n"
                    + "2. Contactez les secours si nécessaire.\n"
                    + "3. Rassemblez les informations et justificatifs utiles.\n"
                    + "4. Déclarez le sinistre à votre assureur dans les délais prévus "
                    + "par votre contrat.\n"
                    + "5. Suivez l'évolution de votre dossier.";
        }

        // DECLARATION DE SINISTRE
        if (contient(q, "declarer", "declaration", "declarer un sinistre",
                "signaler un sinistre")) {
            return "Pour déclarer un sinistre, rendez-vous dans la rubrique Sinistres "
                    + "de votre espace client. Décrivez l'événement, indiquez sa date "
                    + "et joignez les justificatifs nécessaires. Un agent pourra ensuite "
                    + "étudier votre dossier.";
        }

        // DOCUMENTS
        if (contient(q, "documents", "justificatifs", "pieces a fournir",
                "papiers necessaires")) {
            return "Les documents nécessaires dépendent du type de sinistre. "
                    + "Il peut s'agir d'un constat, de photographies, de factures, "
                    + "d'un rapport ou de tout autre justificatif utile. "
                    + "Votre agent pourra vous préciser les documents nécessaires.";
        }

        // SUIVI DU SINISTRE
        if (contient(q, "suivre mon sinistre", "suivi sinistre",
                "etat sinistre", "avancement sinistre", "ou en est mon dossier")) {
            return "Vous pouvez suivre votre dossier dans la rubrique Sinistres "
                    + "de votre espace client. Les statuts possibles sont : déclaré, "
                    + "en analyse, accepté, refusé ou indemnisé.";
        }

        // INDEMNISATION
        if (contient(q, "indemnisation", "indemniser", "remboursement",
                "combien vais-je recevoir")) {
            return "L'indemnisation correspond au montant versé par l'assureur "
                    + "lorsqu'un sinistre est accepté. Elle dépend notamment des "
                    + "garanties souscrites, des plafonds et des franchises prévus "
                    + "dans votre contrat.";
        }

        // REFUS DE SINISTRE
        if (contient(q, "sinistre refuse", "refus de sinistre",
                "indemnisation refusee", "dossier refuse")) {
            return "Si votre sinistre est refusé, consultez d'abord le motif du refus "
                    + "auprès de votre agent. Vous pouvez demander des explications, "
                    + "transmettre des justificatifs complémentaires ou déposer une "
                    + "réclamation selon la procédure prévue par votre assureur.";
        }

        // SINISTRE GENERAL
        if (contient(q, "sinistre", "accident declare")) {
            return "Un sinistre est un événement susceptible d'être couvert par "
                    + "votre assurance. Il doit être déclaré à l'assureur dans les "
                    + "délais prévus au contrat. Son dossier sera ensuite étudié.";
        }

        // PAIEMENTS
        if (contient(q, "paiement", "payer", "regler", "reglement",
                "payer ma cotisation")) {
            return "Vous pouvez consulter vos paiements et vos échéances dans "
                    + "la rubrique Paiements de votre espace client. "
                    + "Vous pourrez y retrouver les montants, les dates et les statuts.";
        }

        // RETARD DE PAIEMENT
        if (contient(q, "retard de paiement", "paiement en retard",
                "paiement impaye", "echeance impayee", "non paiement")) {
            return "En cas de retard de paiement, consultez votre situation dans "
                    + "la rubrique Paiements et contactez votre assureur rapidement. "
                    + "Un impayé peut entraîner des conséquences sur votre contrat "
                    + "selon les conditions contractuelles et la réglementation applicable.";
        }

        // CONTRAT
        if (contient(q, "contrat", "police d'assurance", "numero de contrat")) {
            return "Un contrat d'assurance est un accord entre l'assureur et le client. "
                    + "Il précise les garanties, les cotisations, les dates de validité "
                    + "et les conditions de couverture. Vous pouvez consulter vos contrats "
                    + "dans votre espace client.";
        }

        // RESILIATION
        if (contient(q, "resilier", "resiliation", "annuler mon contrat",
                "arreter mon assurance")) {
            return "Pour résilier votre contrat, vérifiez les conditions de résiliation, "
                    + "les délais et les éventuels justificatifs nécessaires. "
                    + "Contactez votre agent pour connaître la procédure applicable "
                    + "à votre situation.";
        }

        // RENOUVELLEMENT
        if (contient(q, "renouveler", "renouvellement", "reconduction")) {
            return "Le renouvellement permet de prolonger votre contrat d'assurance "
                    + "pour une nouvelle période. Consultez sa date d'expiration et "
                    + "contactez votre agent pour connaître les modalités de renouvellement.";
        }

        // CONTACT AGENT
        if (contient(q, "agent", "conseiller", "contacter", "contact",
                "parler a quelqu'un", "service client")) {
            return "Pour obtenir une assistance personnalisée, contactez votre agent "
                    + "ou le service client de votre assurance. Ils pourront étudier "
                    + "votre situation et répondre à vos questions.";
        }

        // SECURITE
        if (contient(q, "mot de passe", "compte pirate", "securite",
                "informations personnelles", "donnees personnelles")) {
            return "Pour protéger votre compte, utilisez un mot de passe robuste, "
                    + "ne le partagez jamais et déconnectez-vous après utilisation "
                    + "sur un appareil partagé. En cas de suspicion de piratage, "
                    + "contactez rapidement le service responsable.";
        }

        // REMERCIEMENTS ET QUESTIONS NON RECONNUES
        return "Je n'ai pas encore compris votre question. Je peux vous renseigner sur :\n"
                + "- Les types d'assurance et les souscriptions\n"
                + "- Les contrats et les garanties\n"
                + "- Les accidents et les déclarations de sinistres\n"
                + "- Les indemnisations et les remboursements\n"
                + "- Les paiements et les échéances\n"
                + "- Les résiliations et les renouvellements\n"
                + "- Les démarches et les contacts avec un agent.";

    }

    private boolean contient(String question, String... mots) {
        for (String mot : mots) {
            if (question.contains(mot)) {
                return true;
            }
        }
        return false;
    }
}
