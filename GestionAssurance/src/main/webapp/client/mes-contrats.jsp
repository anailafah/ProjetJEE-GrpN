<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes contrats - GestionAssurance</title>

    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f3f5f9;
            color: #263247;
        }

        header {
            background: #17243b;
            color: white;
            padding: 22px 5%;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        header h2 {
            font-size: 22px;
        }

        header a {
            color: white;
            text-decoration: none;
            background: #34445f;
            padding: 10px 16px;
            border-radius: 7px;
        }

        main {
            width: 90%;
            max-width: 1200px;
            margin: 40px auto;
        }

        .titre {
            margin-bottom: 25px;
        }

        .titre h1 {
            font-size: 28px;
            margin-bottom: 8px;
        }

        .titre p {
            color: #718096;
        }

        .table-container {
            background: white;
            border-radius: 12px;
            padding: 25px;
            overflow-x: auto;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 850px;
        }

        th, td {
            padding: 15px 12px;
            text-align: left;
            border-bottom: 1px solid #edf0f5;
        }

        th {
            color: #64748b;
            background: #f8fafc;
            font-size: 13px;
        }

        td {
            font-size: 14px;
        }

        .badge {
            display: inline-block;
            padding: 6px 10px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
        }

        .badge-actif {
            background: #dcfce7;
            color: #15803d;
        }

        .badge-en_attente {
            background: #fef3c7;
            color: #b45309;
        }

        .badge-suspendu {
            background: #ffedd5;
            color: #c2410c;
        }

        .badge-expire, .badge-resilie {
            background: #fee2e2;
            color: #b91c1c;
        }

        .vide {
            text-align: center;
            padding: 45px 15px;
            color: #718096;
        }

        .erreur {
            background: #fee2e2;
            color: #b91c1c;
            padding: 12px;
            border-radius: 7px;
            margin-bottom: 20px;
        }

        .retour {
            display: inline-block;
            margin-bottom: 20px;
            color: #2563eb;
            text-decoration: none;
        }

        @media (max-width: 600px) {
            header {
                padding: 18px 5%;
            }

            header h2 {
                font-size: 18px;
            }

            main {
                margin-top: 25px;
            }

            .table-container {
                padding: 12px;
            }
        }
    </style>
</head>

<body>

<header>
    <h2>GestionAssurance</h2>
    <a href="${pageContext.request.contextPath}/logout">Déconnexion</a>
</header>

<main>

    <a class="retour"
       href="${pageContext.request.contextPath}/client/dashboard">
        ← Retour au tableau de bord
    </a>

    <div class="titre">
        <h1>Mes contrats</h1>
        <p>Consultez les informations de vos contrats d'assurance.</p>
    </div>

    <c:if test="${not empty erreur}">
        <div class="erreur">${erreur}</div>
    </c:if>

    <div class="table-container">

        <c:choose>
            <c:when test="${not empty contrats}">

                <table>
                    <thead>
                        <tr>
                            <th>Numéro</th>
                            <th>Type d'assurance</th>
                            <th>Date de début</th>
                            <th>Date de fin</th>
                            <th>Montant</th>
                            <th>Statut</th>
                            <th>Garanties</th>
                        </tr>
                    </thead>

                    <tbody>
                        <c:forEach var="contrat" items="${contrats}">
                            <tr>
                                <td>${contrat.numeroContrat}</td>
                                <td>${contrat.typeAssurance.nom}</td>
                                <td>${contrat.dateDebut}</td>
                                <td>${contrat.dateFin}</td>
                                <td>${contrat.montant} €</td>

                                <td>
                                    <span class="badge badge-${contrat.statut.toLowerCase()}">
                                        ${contrat.statut}
                                    </span>
                                </td>

                                <td>
                                    <c:choose>
                                        <c:when test="${not empty contrat.garanties}">
                                            <c:forEach var="garantie"
                                                       items="${contrat.garanties}"
                                                       varStatus="boucle">
                                                ${garantie.nom}<c:if test="${!boucle.last}">, </c:if>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            Aucune garantie
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

            </c:when>

            <c:otherwise>
                <div class="vide">
                    <h3>Aucun contrat disponible</h3>
                    <p>Vos contrats apparaîtront ici lorsqu'ils seront enregistrés.</p>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

</main>

</body>
</html>l>