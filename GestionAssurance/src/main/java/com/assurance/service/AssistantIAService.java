package com.assurance.service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.assurance.model.Contrat;
import com.assurance.model.Echeance;
import com.assurance.model.Garantie;
import com.assurance.model.Message;
import com.assurance.model.Paiement;
import com.assurance.model.Sinistre;
import com.assurance.model.TypeAssurance;
import com.google.genai.Client;
import com.google.genai.errors.ApiException;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.HttpOptions;
import com.google.genai.types.HttpRetryOptions;
import com.google.genai.types.ListModelsConfig;
import com.google.genai.types.Model;
import com.google.genai.types.Part;

/**
 * Assistant IA destiné aux clients, basé sur l'API Gemini (Google).
 *
 * À chaque question, on envoie à Gemini :
 *  - des consignes (rôle, règles, fonctionnement de la plateforme) ;
 *  - le dossier du client connecté (contrats, garanties, sinistres, échéances, paiements)
 *    ainsi que l'offre de l'assurance (types d'assurance et garanties disponibles) ;
 *  - l'historique récent de la conversation, pour pouvoir répondre aux questions de suivi.
 *
 * Configuration (variables d'environnement) :
 *  - GEMINI_API_KEY : clé API obtenue sur https://aistudio.google.com/apikey (obligatoire)
 *  - GEMINI_MODEL   : modèle à utiliser (facultatif, "gemini-3.8-flash" par défaut)
 */
public class AssistantIAService {

    private static final String MODELE_PAR_DEFAUT = "gemini-3.8-flash";

    /** Délai maximal d'un appel à un modèle Gemini (en millisecondes). */
    private static final int DELAI_MAX_MS = 20_000;

    /** Nombre maximal de modèles essayés pour une question (principal + secours). */
    private static final int NB_MAX_MODELES = 3;

    /** Modèles de secours, chargés une seule fois (voir modelesDeSecours()). */
    private static volatile List<String> modelesSecours;

    /** Nombre maximal de messages d'historique renvoyés à Gemini. */
    public static final int TAILLE_MAX_HISTORIQUE = 10;

    /** Nombre maximal de paiements listés dans le contexte. */
    private static final int NB_MAX_PAIEMENTS = 10;

    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final String CONSIGNES =
          "Tu es l'assistant virtuel de la plateforme GestionAssurance, une compagnie d'assurance. "
        + "Tu réponds toujours en français, de façon claire, courte et courtoise, en texte simple "
        + "(pas de Markdown, pas de tableaux ; des tirets pour les listes).\n\n"
        + "Règles :\n"
        + "- Pour les questions sur le dossier du client (ses contrats, garanties, sinistres, "
        + "paiements), utilise UNIQUEMENT les données du DOSSIER CLIENT fournies ci-dessous. "
        + "N'invente jamais un contrat, un montant, une date ou un statut.\n"
        + "- Si l'information n'est pas dans le dossier, dis-le et invite le client à contacter son agent.\n"
        + "- Tu ne peux rien modifier : tu expliques au client où faire l'action dans son espace.\n"
        + "- Tu ne parles que d'assurance et de la plateforme. Pour une autre demande, "
        + "réponds poliment que tu ne peux pas aider sur ce sujet.\n"
        + "- Tes réponses sont informatives et ne remplacent pas les conditions du contrat.\n\n"
        + "Fonctionnement de la plateforme (espace client) :\n"
        + "- « Mes contrats » : liste des contrats et détail de chaque contrat avec ses garanties.\n"
        + "- « Mes garanties » : garanties couvertes par chacun de ses contrats.\n"
        + "- « Mes demandes de contrat » : demander un nouveau contrat et suivre ses demandes ; "
        + "une demande est traitée par un agent (statut EN_ATTENTE jusqu'à sa validation).\n"
        + "- « Mes sinistres » puis « Déclarer un sinistre » : choisir le contrat concerné, la date, "
        + "le type de sinistre, décrire les faits, indiquer le montant estimé et joindre les pièces "
        + "justificatives. Le suivi se fait ensuite dans « Mes sinistres ».\n"
        + "- Étapes d'un sinistre : Déclaré, puis En cours d'analyse par un agent, puis Accepté ou Refusé, "
        + "puis Indemnisé (versement du montant d'indemnisation).\n"
        + "- « Mes paiements » : échéances à payer, paiements effectués et historique.\n"
        + "- « Mon profil » : modifier ses informations personnelles et son mot de passe.\n"
        + "- Statuts d'un contrat : EN_ATTENTE, ACTIF, SUSPENDU, EXPIRE, RESILIE.\n\n"
        + "Pièces justificatives habituelles selon le type de sinistre :\n"
        + "- Accident (automobile) : constat amiable, photos des dégâts, devis ou facture de réparation, "
        + "permis de conduire et carte grise.\n"
        + "- Vol : récépissé de dépôt de plainte, preuves d'achat ou factures des biens volés, photos.\n"
        + "- Incendie : photos des dégâts, factures des biens endommagés, rapport des pompiers si disponible.\n"
        + "- Dégâts des eaux : constat de dégât des eaux, photos, devis ou factures de réparation.\n"
        + "- Maladie / frais médicaux : ordonnances, factures médicales, feuilles de soins.\n"
        + "- Incident de voyage : billets, justificatifs de retard ou d'annulation, factures des frais engagés.\n"
        + "Précise que l'agent peut demander des documents complémentaires.";

    private static volatile Client client;

    private final ContratService contratService = new ContratService();
    private final SinistreService sinistreService = new SinistreService();
    private final EcheanceService echeanceService = new EcheanceService();
    private final PaiementService paiementService = new PaiementService();
    private final TypeAssuranceService typeAssuranceService = new TypeAssuranceService();
    private final GarantieService garantieService = new GarantieService();

    /**
     * Répond à la question d'un client.
     *
     * @param clientId   identifiant du client connecté
     * @param nomClient  prénom et nom du client (pour personnaliser la réponse)
     * @param historique messages précédents de la conversation (peut être vide)
     * @param question   nouvelle question du client
     * @return la réponse de l'assistant (jamais null)
     * @throws ServiceException si l'assistant n'est pas configuré ou si Gemini est indisponible
     */
    public String repondre(Long clientId, String nomClient, List<Message> historique, String question) {

        long debutContexte = System.currentTimeMillis();
        String instructions = CONSIGNES
            + "\n\n=== DOSSIER CLIENT (données à jour au " + date(LocalDate.now()) + ") ===\n"
            + construireDossierClient(clientId, nomClient)
            + "\n=== OFFRE DE L'ASSURANCE ===\n"
            + construireOffre();
        System.out.println("AssistantIAService : dossier client chargé en "
            + (System.currentTimeMillis() - debutContexte) + " ms");

        GenerateContentConfig config = GenerateContentConfig.builder()
            .systemInstruction(Content.fromParts(Part.fromText(instructions)))
            .temperature(0.3f)
            .maxOutputTokens(2048)
            .build();

        List<Content> conversation = new ArrayList<>();
        int debut = Math.max(0, historique.size() - TAILLE_MAX_HISTORIQUE);
        for (Message message : historique.subList(debut, historique.size())) {
            conversation.add(contenu(message.isClient() ? "user" : "model", message.getTexte()));
        }
        conversation.add(contenu("user", question));

        GenerateContentResponse reponse = appelerGemini(conversation, config);

        String texte = reponse.text();
        if (texte == null || texte.isBlank()) {
            return "Je n'ai pas réussi à formuler une réponse. Pouvez-vous reformuler votre question ?";
        }
        return texte.trim();
    }

    // ------------------------------------------------------------------
    // Appel à Gemini, avec bascule sur un autre modèle en cas de surcharge
    // ------------------------------------------------------------------

    /**
     * Appelle le modèle principal ; s'il est surchargé (503), limité (429), indisponible (404)
     * ou trop lent, essaie les modèles de secours, dans la limite de NB_MAX_MODELES essais.
     */
    private GenerateContentResponse appelerGemini(List<Content> conversation, GenerateContentConfig config) {

        List<String> modeles = new ArrayList<>();
        modeles.add(modele());
        for (String secours : modelesDeSecours()) {
            if (modeles.size() >= NB_MAX_MODELES) {
                break;
            }
            if (!modeles.contains(secours)) {
                modeles.add(secours);
            }
        }

        boolean surcharge = false;

        for (String modele : modeles) {
            long debut = System.currentTimeMillis();
            try {
                GenerateContentResponse reponse =
                    obtenirClient().models.generateContent(modele, conversation, config);
                System.out.println("AssistantIAService : réponse de " + modele + " en "
                    + (System.currentTimeMillis() - debut) + " ms");
                return reponse;

            } catch (ApiException e) {
                System.err.println("AssistantIAService : " + modele + " a répondu " + e.code()
                    + " après " + (System.currentTimeMillis() - debut) + " ms : " + e.getMessage());
                // 404 : modèle retiré ; 429 : quota dépassé ; 5xx (500, 503, 504...) : Google surchargé
                if (e.code() == 404 || e.code() == 429 || e.code() >= 500) {
                    surcharge |= (e.code() != 404);
                    continue; // modèle suivant
                }
                throw new ServiceException(
                    "L'assistant IA est momentanément indisponible. Veuillez réessayer dans quelques instants.");

            } catch (ServiceException e) {
                throw e;

            } catch (Exception e) {
                // Délai dépassé, problème réseau...
                System.err.println("AssistantIAService : échec de l'appel à " + modele + " après "
                    + (System.currentTimeMillis() - debut) + " ms : " + e);
                surcharge = true;
            }
        }

        throw new ServiceException(surcharge
            ? "L'assistant IA est très sollicité en ce moment. Veuillez réessayer dans une minute."
            : "L'assistant IA est momentanément indisponible. Veuillez réessayer dans quelques instants.");
    }

    /**
     * Modèles « flash » de génération de texte disponibles avec la clé API,
     * récupérés une seule fois auprès de Google puis gardés en mémoire.
     * Les plus récents (numéro de version le plus élevé) sont essayés en premier.
     */
    private static List<String> modelesDeSecours() {
        if (modelesSecours == null) {
            synchronized (AssistantIAService.class) {
                if (modelesSecours == null) {
                    List<String> trouves = new ArrayList<>();
                    try {
                        for (Model m : obtenirClient().models.list(ListModelsConfig.builder().pageSize(100).build())) {
                            String nom = m.name().orElse("").replace("models/", "");
                            boolean genereDuTexte = m.supportedActions()
                                .map(actions -> actions.contains("generateContent"))
                                .orElse(false);
                            if (genereDuTexte && nom.startsWith("gemini") && nom.contains("flash")
                                    && !nom.matches(".*(preview|exp|tts|image|audio|live|embedding|vision|robotics|computer).*")) {
                                trouves.add(nom);
                            }
                        }
                        trouves.sort(Comparator.reverseOrder());
                        System.out.println("AssistantIAService : modèles de secours disponibles : " + trouves);
                    } catch (Exception e) {
                        // Pas de mise en cache : on réessaiera de lister les modèles à la prochaine question
                        System.err.println("AssistantIAService : impossible de lister les modèles : " + e);
                        return List.of();
                    }
                    modelesSecours = trouves;
                }
            }
        }
        return modelesSecours;
    }

    // ------------------------------------------------------------------
    // Contexte envoyé à Gemini
    // ------------------------------------------------------------------

    private String construireDossierClient(Long clientId, String nomClient) {

        StringBuilder sb = new StringBuilder();
        sb.append("Client : ").append(nomClient).append('\n');

        // Contrats et garanties
        List<Contrat> contrats = contratService.listerParClient(clientId);
        sb.append("\nCONTRATS (").append(contrats.size()).append(") :\n");
        if (contrats.isEmpty()) {
            sb.append("- Aucun contrat.\n");
        }
        for (Contrat c : contrats) {
            sb.append("- Contrat n° ").append(c.getNumeroContrat())
              .append(" | type : ").append(c.getTypeAssurance() != null ? c.getTypeAssurance().getNom() : "?")
              .append(" | statut : ").append(c.getStatut())
              .append(" | du ").append(date(c.getDateDebut()))
              .append(" au ").append(date(c.getDateFin()))
              .append(" | prime : ").append(euros(c.getMontant()))
              .append('\n');

            if (c.getGaranties() == null || c.getGaranties().isEmpty()) {
                sb.append("    Garanties : aucune\n");
            } else {
                sb.append("    Garanties :\n");
                for (Garantie g : c.getGaranties()) {
                    sb.append("      * ").append(decrire(g)).append('\n');
                }
            }
        }

        // Sinistres (du plus récent au plus ancien)
        List<Sinistre> sinistres = sinistreService.listerParClient(clientId);
        sb.append("\nSINISTRES (").append(sinistres.size()).append(", du plus récent au plus ancien) :\n");
        if (sinistres.isEmpty()) {
            sb.append("- Aucun sinistre déclaré.\n");
        }
        for (Sinistre s : sinistres) {
            sb.append("- Sinistre n° ").append(s.getNumeroSinistre())
              .append(" | déclaré le ").append(date(s.getDateDeclaration()))
              .append(" | survenu le ").append(date(s.getDateSinistre()))
              .append(" | type : ").append(s.getTypeSinistre() != null ? s.getTypeSinistre().getLibelle() : "?")
              .append(" | statut : ").append(s.getStatut() != null ? s.getStatut().getLibelle() : "?")
              .append(" | contrat n° ").append(s.getContrat() != null ? s.getContrat().getNumeroContrat() : "?")
              .append(" | montant estimé : ").append(euros(s.getMontantDemande()));
            if (s.getMontantIndemnisation() != null) {
                sb.append(" | indemnisation : ").append(euros(s.getMontantIndemnisation()));
            }
            if (s.getDescription() != null && !s.getDescription().isBlank()) {
                sb.append(" | description : ").append(resume(s.getDescription(), 200));
            }
            sb.append('\n');
        }

        // Échéances
        List<Echeance> echeances = echeanceService.listerParClient(clientId);
        sb.append("\nÉCHÉANCES (").append(echeances.size()).append(") :\n");
        if (echeances.isEmpty()) {
            sb.append("- Aucune échéance.\n");
        }
        for (Echeance e : echeances) {
            sb.append("- ").append(date(e.getDateEcheance()))
              .append(" | contrat n° ").append(e.getContrat() != null ? e.getContrat().getNumeroContrat() : "?")
              .append(" | montant : ").append(euros(e.getMontant()))
              .append(" | statut : ").append(e.getStatut())
              .append(" | reste à payer : ").append(euros(paiementService.resteAPayer(e)))
              .append('\n');
        }

        // Derniers paiements
        List<Paiement> paiements = paiementService.historiqueParClient(clientId);
        sb.append("\nDERNIERS PAIEMENTS :\n");
        if (paiements.isEmpty()) {
            sb.append("- Aucun paiement.\n");
        }
        for (Paiement p : paiements.subList(0, Math.min(NB_MAX_PAIEMENTS, paiements.size()))) {
            sb.append("- ").append(date(p.getDatePaiement()))
              .append(" | ").append(euros(p.getMontant()))
              .append(" | mode : ").append(p.getModePaiement())
              .append(" | statut : ").append(p.getStatut())
              .append('\n');
        }

        return sb.toString();
    }

    private String construireOffre() {

        StringBuilder sb = new StringBuilder("Types d'assurance proposés :\n");
        for (TypeAssurance t : typeAssuranceService.lister()) {
            if (!t.isActif()) {
                continue;
            }
            sb.append("- ").append(t.getNom())
              .append(" | tarif de base : ").append(euros(t.getTarifBase()));
            if (t.getDureeMois() != null) {
                sb.append(" | durée : ").append(t.getDureeMois()).append(" mois");
            }
            if (t.getDescription() != null && !t.getDescription().isBlank()) {
                sb.append(" | ").append(resume(t.getDescription(), 200));
            }
            sb.append('\n');
        }

        sb.append("\nGaranties existantes :\n");
        for (Garantie g : garantieService.lister()) {
            if (g.isActif()) {
                sb.append("- ").append(decrire(g)).append('\n');
            }
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------
    // Utilitaires
    // ------------------------------------------------------------------

    private static Client obtenirClient() {
        if (client == null) {
            synchronized (AssistantIAService.class) {
                if (client == null) {
                    String cle = System.getenv("GEMINI_API_KEY");
                    if (cle == null || cle.isBlank()) {
                        System.err.println("AssistantIAService : variable d'environnement GEMINI_API_KEY absente.");
                        throw new ServiceException(
                            "L'assistant IA n'est pas encore configuré. Veuillez contacter votre agent.");
                    }
                    // Par défaut le SDK n'a aucun délai maximum et réessaie plusieurs fois :
                    // si Gemini ne répond pas, la page charge indéfiniment. On limite donc
                    // chaque appel à DELAI_MAX_MS, sans nouvelle tentative sur le même modèle
                    // (en cas d'échec, appelerGemini() passe au modèle de secours suivant).
                    HttpOptions options = HttpOptions.builder()
                        .timeout(DELAI_MAX_MS)
                        .retryOptions(HttpRetryOptions.builder().attempts(1).build())
                        .build();
                    client = Client.builder().apiKey(cle).httpOptions(options).build();
                }
            }
        }
        return client;
    }

    private static String modele() {
        String modele = System.getenv("GEMINI_MODEL");
        return (modele == null || modele.isBlank()) ? MODELE_PAR_DEFAUT : modele;
    }

    private static Content contenu(String role, String texte) {
        return Content.builder().role(role).parts(Part.fromText(texte)).build();
    }

    private static String decrire(Garantie g) {
        StringBuilder sb = new StringBuilder(g.getNom());
        if (g.getMontantCouverture() != null) {
            sb.append(" (plafond : ").append(euros(g.getMontantCouverture())).append(')');
        }
        if (g.getDescription() != null && !g.getDescription().isBlank()) {
            sb.append(" : ").append(resume(g.getDescription(), 200));
        }
        return sb.toString();
    }

    private static String date(TemporalAccessor date) {
        return date == null ? "non renseignée" : FORMAT_DATE.format(date);
    }

    private static String euros(BigDecimal montant) {
        if (montant == null) {
            return "non renseigné";
        }
        return NumberFormat.getCurrencyInstance(Locale.FRANCE).format(montant);
    }

    private static String resume(String texte, int longueurMax) {
        String t = texte.replaceAll("\\s+", " ").trim();
        return t.length() <= longueurMax ? t : t.substring(0, longueurMax) + "…";
    }
}
