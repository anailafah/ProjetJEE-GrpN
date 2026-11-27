<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Mes demandes - GestionAssurance</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Arial, sans-serif;
        }

        body {
            background: #f3f6fb;
            color: #26344a;
        }

        header {
            background: #142b4a;
            color: white;
            padding: 22px 5%;
        }

        header h2 {
            font-size: 23px;
        }

        .container {
            width: 90%;
            max-width: 1150px;
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
            margin-bottom: 12px;
        }

        .description {
            color: #8290a3;
            margin-bottom: 25px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
        }

        th {
            background: #142b4a;
            color: white;
            padding: 14px;
            text-align: left;
        }

        td {
            padding: 14px;
            border-bottom: 1px solid #e9edf4;
        }

        tr:hover {
            background: #f7f9fc;
        }

        .statut {
            display: inline-block;
            padding: 7px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
        }

        .attente {
            background: #fff0df;
            color: #c47b20;
        }

        .accepte {
            background: #e1f8f2;
            color: #15966f;
        }

        .refuse {
            background: #ffe9eb;
            color: #d64550;
        }

        .autre {
            background: #e7efff;
            color: #315d9b;
        }

        .empty {
            padding: 25px;
            background: #f7f9fc;
            border-radius: 10px;
            color: #8290a3;
        }

        .actions {
            margin-top: 25px;
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
        }

        .btn {
            display: inline-block;
            padding: 11px 20px;
            background: linear-gradient(120deg, #142b4a, #1d5275);
            color: white;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
            font-size: 14px;
        }

        .btn:hover {
            opacity: 0.9;
        }

        .table-container {
            overflow-x: auto;
        }
    </style>
</head>

<body>

<header>
    <h2>Gestion<span style="color:#35d0c2">Assurance</span></h2>
</header>

<main class="container">

    <div class="card">

        <h1>Mes demandes de contrat</h1>

        <p class="description">
            Consultez vos demandes de souscription et suivez leur évolution.
        </p>

        <c:if test="${empty contrats}">
            <div class="empty">
                Vous n'avez encore aucune demande de contrat.
            </div>
        </c:if>

        <c:if test="${not empty contrats}">

            <div class="table-container">

                <table>
                    <thead>
                        <tr>
                            <th>Numéro</th>
                            <th>Type d'assurance</th>
                            <th>Date de début</th>
                            <th>Date de fin</th>
                            <th>Montant</th>
                            <th>Statut</th>
                        </tr>
                    </thead>

                    <tbody>

                        <c:forEach var="contrat" items="${contrats}">

                            <tr>
                                <td>${contrat.numeroContrat}</td>

                                <td>${contrat.typeAssurance.nom}</td>

                                <td>${contrat.dateDebut}</td>

                                <td>${contrat.dateFin}</td>

                                <td>${contrat.montant}</td>

                                <td>
                                    <c:choose>

                                        <c:when test="${contrat.statut == 'EN_ATTENTE'}">
                                            <span class="statut attente">
                                                En attente
                                            </span>
                                        </c:when>

                                        <c:when test="${contrat.statut == 'ACTIF'}">
                                            <span class="statut accepte">
                                                Acceptée
                                            </span>
                                        </c:when>

                                        <c:when test="${contrat.statut == 'REFUSE'}">
                                            <span class="statut refuse">
                                                Refusée
                                            </span>
                                        </c:when>

                                        <c:otherwise>
                                            <span class="statut autre">
                                                ${contrat.statut}
                                            </span>
                                        </c:otherwise>

                                    </c:choose>
                                </td>

                            </tr>

                        </c:forEach>

                    </tbody>
                </table>

            </div>

        </c:if>

        <div class="actions">

            <a class="btn"
               href="${pageContext.request.contextPath}/client/demande-contrat">
                Faire une nouvelle demande
            </a>

            <a class="btn"
               href="${pageContext.request.contextPath}/client/dashboard">
                Retour au tableau de bord
            </a>

        </div>

    </div>

</main>

</body>
</html>