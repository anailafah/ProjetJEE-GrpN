<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Assistant virtuel - Gestion Assurance</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/assistant-ia.css">
</head>

<body>
<div class="retour-dashboard">
    <a href="${pageContext.request.contextPath}/client/dashboard">
        Retour au tableau de bord
    </a>
</div>
<div class="page-container">

    <header class="assistant-header">

        <div class="assistant-icon">IA</div>

        <div>
            <h1>Assistant virtuel</h1>
            <p>Votre conseiller en assurance</p>
        </div>

    </header>


    <main class="chat-container">

        <section class="welcome-message">

            <h2>Bienvenue !</h2>

            <p>
                Je suis votre assistant virtuel.
                Je peux répondre à vos questions concernant les assurances,
                les contrats, les garanties, les sinistres et les paiements.
            </p>

        </section>


        <section class="suggestions">

            <h3>Questions fréquentes</h3>

            <div class="suggestion-buttons">

                <button type="button"
                        onclick="poserQuestion('Quels types d assurance proposez-vous ?')">
                    Types d'assurance
                </button>

                <button type="button"
                        onclick="poserQuestion('Comment souscrire une assurance ?')">
                    Souscription
                </button>

                <button type="button"
                        onclick="poserQuestion('Qu est-ce qu une garantie ?')">
                    Garanties
                </button>

                <button type="button"
                        onclick="poserQuestion('Comment déclarer un sinistre ?')">
                    Déclarer un sinistre
                </button>

                <button type="button"
                        onclick="poserQuestion('Comment fonctionne l indemnisation ?')">
                    Indemnisation
                </button>

                <button type="button"
                        onclick="poserQuestion('Comment payer ma cotisation ?')">
                    Paiements
                </button>

                <button type="button"
                        onclick="poserQuestion('Comment résilier mon contrat ?')">
                    Résiliation
                </button>

                <button type="button"
                        onclick="poserQuestion('Comment contacter un agent ?')">
                    Contacter un agent
                </button>

            </div>

        </section>


        <section class="conversation">

            <% if (request.getAttribute("reponse") != null) { %>

                <div class="user-message">

                    <strong>Vous</strong>

                    <p>${question}</p>

                </div>


                <div class="assistant-message">

                    <strong>Assistant virtuel</strong>

                    <p>${reponse}</p>

                </div>

            <% } else { %>

                <div class="assistant-message">

                    <strong>Assistant virtuel</strong>

                    <p>
                        Posez votre question dans le champ ci-dessous
                        ou sélectionnez une question fréquente.
                    </p>

                </div>

            <% } %>

        </section>


        <form action="${pageContext.request.contextPath}/assistant-ia"
              method="post"
              class="question-form">

            <label for="question">Votre question</label>

            <textarea
                id="question"
                name="question"
                placeholder="Écrivez votre question ici..."
                required>${question}</textarea>

            <button type="submit">
                Envoyer
            </button>

        </form>

    </main>

</div>


<script>

    function poserQuestion(question) {

        document.getElementById("question").value = question;

        document.getElementById("question").focus();

    }

</script>

</body>
</html>