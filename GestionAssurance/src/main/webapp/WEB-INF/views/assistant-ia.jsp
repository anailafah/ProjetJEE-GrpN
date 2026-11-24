<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Assistant IA - Gestion Assurance</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/assistant-ia.css">
</head>
<body>

    <h1>Assistant virtuel</h1>
    <p>Posez vos questions sur la gestion de vos assurances.</p>

    <div class="chat-container">

        <form action="${pageContext.request.contextPath}/assistant-ia" method="post">

            <label for="question">Votre question :</label>

            <textarea id="question" name="question"
                      placeholder="Exemple : Qu'est-ce qu'un contrat d'assurance ?"
                      required>${question}</textarea>

            <button type="submit">Envoyer</button>

        </form>

        <div class="response">
            <h3>Réponse de l'assistant :</h3>
            <p>${reponse}</p>
        </div>

    </div>

</body>
</html>