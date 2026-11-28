<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Assistant IA - Gestion Assurance</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Arial, sans-serif;
            background: #f3f5fa;
            color: #263248;
            min-height: 100vh;
        }

        .header {
            background: #17243d;
            color: white;
            padding: 18px 7%;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .logo {
            font-size: 21px;
            font-weight: 700;
        }

        .back-link {
            color: white;
            text-decoration: none;
            background: rgba(255,255,255,0.12);
            padding: 10px 16px;
            border-radius: 8px;
            font-size: 14px;
        }

        .back-link:hover {
            background: rgba(255,255,255,0.22);
        }

        .container {
            max-width: 1000px;
            margin: 40px auto;
            padding: 0 20px;
        }

        .intro {
            margin-bottom: 25px;
        }

        .intro h1 {
            font-size: 28px;
            color: #17243d;
            margin-bottom: 8px;
        }

        .intro p {
            color: #718096;
            line-height: 1.6;
        }

        .chat-card {
            background: white;
            border-radius: 16px;
            box-shadow: 0 8px 30px rgba(30, 45, 75, 0.08);
            overflow: hidden;
        }

        .chat-header {
            background: #263b61;
            color: white;
            padding: 20px 25px;
            display: flex;
            align-items: center;
            gap: 14px;
        }

        .ai-icon {
            width: 45px;
            height: 45px;
            border-radius: 13px;
            background: #ffffff;
            color: #263b61;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 800;
            font-size: 17px;
        }

        .chat-header h2 {
            font-size: 17px;
            margin-bottom: 4px;
        }

        .chat-header p {
            font-size: 13px;
            color: #d7e2f4;
        }

        .status-dot {
            display: inline-block;
            width: 8px;
            height: 8px;
            background: #53d69a;
            border-radius: 50%;
            margin-right: 5px;
        }

        .chat-body {
            padding: 28px;
            min-height: 300px;
        }

        .welcome {
            background: #f2f6ff;
            border: 1px solid #e1e9f8;
            border-radius: 12px;
            padding: 20px;
            margin-bottom: 25px;
            line-height: 1.7;
        }

        .welcome strong {
            color: #263b61;
        }

        .suggestions-title {
            font-size: 15px;
            margin-bottom: 13px;
            color: #263b61;
        }

        .suggestions {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            margin-bottom: 25px;
        }

        .suggestions button {
            border: 1px solid #dce4f1;
            background: white;
            color: #344766;
            padding: 10px 14px;
            border-radius: 20px;
            cursor: pointer;
            font-size: 13px;
            transition: 0.2s;
        }

        .suggestions button:hover {
            background: #edf3ff;
            border-color: #7897cb;
        }

        .conversation {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        .message {
            max-width: 85%;
            padding: 16px 18px;
            border-radius: 13px;
            line-height: 1.7;
            overflow-wrap: anywhere;
        }

        .user-message {
            align-self: flex-end;
            background: #263b61;
            color: white;
            border-bottom-right-radius: 3px;
        }

        .assistant-message {
            align-self: flex-start;
            background: #f0f3f8;
            color: #34445d;
            border-bottom-left-radius: 3px;
        }

        .message strong {
            display: block;
            margin-bottom: 7px;
            font-size: 13px;
        }

        .message p {
            font-size: 14px;
            white-space: pre-wrap;
        }

        .chat-actions {
            display: flex;
            justify-content: flex-end;
            margin-bottom: 15px;
        }

        .reset-button {
            border: 1px solid #dce4f1;
            background: white;
            color: #344766;
            padding: 8px 14px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 13px;
        }

        .reset-button:hover {
            background: #edf3ff;
        }

        .waiting {
            display: none;
            align-self: flex-start;
            background: #f0f3f8;
            color: #34445d;
            padding: 14px 18px;
            border-radius: 13px;
            border-bottom-left-radius: 3px;
            font-size: 14px;
        }

        .waiting::after {
            content: "";
            animation: points 1.2s steps(4, end) infinite;
        }

        @keyframes points {
            0%   { content: ""; }
            25%  { content: "."; }
            50%  { content: ".."; }
            75%  { content: "..."; }
        }

        .error-message {
            background: #fff1f0;
            color: #a83232;
            border: 1px solid #ffd5d1;
            padding: 13px;
            border-radius: 9px;
            margin-bottom: 15px;
        }

        .question-area {
            border-top: 1px solid #edf0f5;
            padding: 22px 28px;
            background: #fcfcfe;
        }

        .question-area label {
            display: block;
            font-size: 14px;
            font-weight: 600;
            margin-bottom: 10px;
            color: #263b61;
        }

        .input-row {
            display: flex;
            gap: 12px;
            align-items: stretch;
        }

        .input-row textarea {
            flex: 1;
            min-height: 55px;
            max-height: 150px;
            resize: vertical;
            padding: 15px;
            border: 1px solid #dce3ee;
            border-radius: 10px;
            font-family: inherit;
            font-size: 14px;
            outline: none;
        }

        .input-row textarea:focus {
            border-color: #5479b5;
            box-shadow: 0 0 0 3px rgba(84,121,181,0.12);
        }

        .send-button {
            border: none;
            background: #263b61;
            color: white;
            padding: 0 25px;
            border-radius: 10px;
            font-weight: 600;
            cursor: pointer;
            transition: 0.2s;
        }

        .send-button:hover {
            background: #385582;
        }

        .send-button:disabled {
            opacity: 0.6;
            cursor: not-allowed;
        }

        .disclaimer {
            margin-top: 13px;
            color: #8993a3;
            font-size: 12px;
            line-height: 1.5;
        }

        @media (max-width: 650px) {
            .header {
                padding: 16px 5%;
            }

            .container {
                margin: 25px auto;
            }

            .chat-body {
                padding: 20px;
            }

            .question-area {
                padding: 18px;
            }

            .input-row {
                flex-direction: column;
            }

            .send-button {
                padding: 14px;
            }

            .message {
                max-width: 100%;
            }
        }
    </style>
</head>

<body>

<header class="header">
    <div class="logo">Gestion Assurance</div>

    <a class="back-link"
       href="${pageContext.request.contextPath}/client/dashboard">
        ← Tableau de bord
    </a>
</header>

<div class="container">

    <div class="intro">
        <h1>Votre assistant IA</h1>
        <p>
            Besoin d'une information sur votre assurance ?
            Posez votre question et obtenez une réponse instantanément.
        </p>
    </div>

    <div class="chat-card">

        <div class="chat-header">
            <div class="ai-icon">IA</div>

            <div>
                <h2>Assistant virtuel</h2>
                <p><span class="status-dot"></span>Assistant spécialisé en assurance</p>
            </div>
        </div>

        <div class="chat-body">

            <div class="welcome">
                <strong>Bonjour et bienvenue !</strong><br>
                Je connais vos contrats, vos garanties, vos sinistres et vos paiements :
                posez-moi vos questions sur votre dossier ou sur le fonctionnement de votre assurance.
            </div>

            <c:if test="${empty conversation}">
                <h3 class="suggestions-title">Quelques questions pour commencer :</h3>

                <div class="suggestions">
                    <button type="button" onclick="poserQuestion(this.textContent)">Quels sont mes contrats actifs ?</button>
                    <button type="button" onclick="poserQuestion(this.textContent)">Quelles garanties possède mon assurance automobile ?</button>
                    <button type="button" onclick="poserQuestion(this.textContent)">Comment déclarer un sinistre ?</button>
                    <button type="button" onclick="poserQuestion(this.textContent)">Quels documents dois-je fournir pour mon sinistre ?</button>
                    <button type="button" onclick="poserQuestion(this.textContent)">Quelle est la différence entre la garantie vol et la garantie incendie ?</button>
                    <button type="button" onclick="poserQuestion(this.textContent)">Où en est mon dernier sinistre ?</button>
                </div>
            </c:if>

            <c:if test="${not empty conversation}">
                <form class="chat-actions"
                      action="${pageContext.request.contextPath}/assistant-ia"
                      method="post">
                    <input type="hidden" name="action" value="reinitialiser">
                    <button type="submit" class="reset-button">Nouvelle conversation</button>
                </form>
            </c:if>

            <div class="conversation" id="conversation">

                <c:forEach var="message" items="${conversation}">
                    <div class="message ${message.client ? 'user-message' : 'assistant-message'}">
                        <strong>${message.client ? 'Vous' : 'Assistant IA'}</strong>
                        <p><c:out value="${message.texte}"/></p>
                    </div>
                </c:forEach>

                <div class="waiting" id="attente">L'assistant consulte votre dossier</div>

                <c:if test="${not empty erreur}">
                    <div class="error-message">
                        <c:out value="${erreur}"/>
                    </div>
                </c:if>

            </div>

        </div>

        <div class="question-area">

            <form id="formulaireQuestion"
                  action="${pageContext.request.contextPath}/assistant-ia"
                  method="post"
                  onsubmit="envoiEnCours();">

                <label for="question">Votre question</label>

                <div class="input-row">
                    <textarea
                        id="question"
                        name="question"
                        maxlength="1000"
                        placeholder="Écrivez votre question ici..."
                        required><c:out value="${question}"/></textarea>

                    <button type="submit" id="envoyer" class="send-button">
                        Envoyer ➤
                    </button>
                </div>

                <p class="disclaimer">
                    Les réponses de l'assistant sont fournies à titre informatif.
                    Pour une décision concernant votre contrat, contactez votre agent.
                </p>

            </form>

        </div>

    </div>

</div>

<script>
    const champQuestion = document.getElementById("question");
    const formulaire = document.getElementById("formulaireQuestion");

    const boutonEnvoyer = document.getElementById("envoyer");
    const texteBouton = boutonEnvoyer.innerHTML;
    const attente = document.getElementById("attente");

    // Pendant l'appel à l'IA : bouton désactivé (évite un double envoi) et message d'attente visible
    function envoiEnCours() {
        boutonEnvoyer.disabled = true;
        boutonEnvoyer.textContent = "Envoi…";
        attente.style.display = "block";
        attente.scrollIntoView({ block: "center", behavior: "smooth" });
    }

    // Retour sur la page avec le bouton "Précédent" du navigateur : on réactive le formulaire
    window.addEventListener("pageshow", function () {
        boutonEnvoyer.disabled = false;
        boutonEnvoyer.innerHTML = texteBouton;
        attente.style.display = "none";
    });

    // Les boutons de suggestion envoient directement la question
    function poserQuestion(question) {
        champQuestion.value = question.trim();
        envoiEnCours();
        formulaire.submit();
    }

    // Entrée envoie la question, Maj + Entrée ajoute une ligne
    champQuestion.addEventListener("keydown", function (e) {
        if (e.key === "Enter" && !e.shiftKey && champQuestion.value.trim() !== "") {
            e.preventDefault();
            envoiEnCours();
            formulaire.submit();
        }
    });

    // Afficher le dernier message
    const messages = document.querySelectorAll("#conversation .message");
    if (messages.length > 0) {
        messages[messages.length - 1].scrollIntoView({ block: "nearest" });
    }
    champQuestion.focus();
</script>

</body>
</html>