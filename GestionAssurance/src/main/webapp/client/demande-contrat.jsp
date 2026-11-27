<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Demande de contrat - GestionAssurance</title>

    <style>
        * {
            box-sizing: border-box;
            font-family: 'Segoe UI', Arial, sans-serif;
        }

        body {
            margin: 0;
            background: #f3f6fb;
            color: #26344a;
        }

        header {
            background: #142b4a;
            color: white;
            padding: 22px 5%;
        }

        header h2 {
            margin: 0;
        }

        .container {
            width: 90%;
            max-width: 750px;
            margin: 40px auto;
        }

        .card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 5px 18px rgba(25,45,75,0.06);
        }

        h1 {
            color: #142b4a;
            margin-bottom: 10px;
        }

        .description {
            color: #8290a3;
            margin-bottom: 25px;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
        }

        input, select {
            width: 100%;
            padding: 12px;
            border: 1px solid #dce3ed;
            border-radius: 8px;
            font-size: 14px;
        }

        input[type="checkbox"] {
            width: auto;
        }

        .garantie {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-bottom: 10px;
            font-weight: normal;
        }

        .garantie label {
            margin: 0;
            font-weight: normal;
        }

        .message-success {
            background: #e1f8f2;
            color: #15966f;
            padding: 13px;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .message-error {
            background: #ffe9eb;
            color: #d64550;
            padding: 13px;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .btn {
            display: inline-block;
            padding: 12px 20px;
            background: linear-gradient(120deg, #142b4a, #1d5275);
            color: white;
            border: none;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            cursor: pointer;
        }

        .btn:hover {
            opacity: 0.9;
        }

        .actions {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
            margin-top: 25px;
        }
    </style>
</head>

<body>

<header>
    <h2>Gestion<span style="color:#35d0c2">Assurance</span></h2>
</header>

<main class="container">

    <div class="card">

        <h1>Demander un nouveau contrat</h1>

        <p class="description">
            Remplissez le formulaire pour envoyer votre demande de souscription.
        </p>

        <!-- MESSAGE DE SUCCÈS -->
        <c:if test="${not empty sessionScope.messageSucces}">
            <div class="message-success">
                <c:out value="${sessionScope.messageSucces}"/>
            </div>
            <c:remove var="messageSucces" scope="session"/>
        </c:if>

        <!-- MESSAGE D'ERREUR -->
        <c:if test="${not empty sessionScope.messageErreur}">
            <div class="message-error">
                <c:out value="${sessionScope.messageErreur}"/>
            </div>
            <c:remove var="messageErreur" scope="session"/>
        </c:if>

        <form action="${pageContext.request.contextPath}/client/soumettre-demande"
              method="post">

            <div class="form-group">

                <label>Type d'assurance :</label>

                <select name="typeAssuranceId" required>

                    <option value="">-- Choisir une assurance --</option>

                    <c:forEach var="type" items="${typesAssurance}">
                        <option value="${type.id}">
                            <c:out value="${type.nom}"/>
                        </option>
                    </c:forEach>

                </select>

            </div>

            <div class="form-group">

                <label>Garanties souhaitées :</label>

                <c:choose>
                    <c:when test="${not empty garanties}">

                        <c:forEach var="garantie" items="${garanties}">

                            <div class="garantie">
                                <input type="checkbox"
                                       name="garanties"
                                       value="${garantie.id}"
                                       id="garantie${garantie.id}">

                                <label for="garantie${garantie.id}">
                                    <c:out value="${garantie.nom}"/>
                                </label>
                            </div>

                        </c:forEach>

                    </c:when>

                    <c:otherwise>
                        <p>Aucune garantie disponible actuellement.</p>
                    </c:otherwise>
                </c:choose>

            </div>

            <div class="form-group">

                <label>Date de début :</label>

                <input type="date"
                       name="dateDebut"
                       min="<%= java.time.LocalDate.now() %>"
                       required>

            </div>

            <div class="form-group">

                <label>Date de fin :</label>

                <input type="date"
                       name="dateFin"
                       required>

            </div>

            <div class="form-group">

                <label>Montant :</label>

                <input type="number"
                       name="montant"
                       step="0.01"
                       min="0.01"
                       required>

            </div>

            <div class="actions">

                <button type="submit" class="btn">
                    Envoyer ma demande
                </button>

                <a class="btn"
                   href="${pageContext.request.contextPath}/client/dashboard">
                    Retour
                </a>

            </div>

        </form>

    </div>

</main>

</body>
</html>