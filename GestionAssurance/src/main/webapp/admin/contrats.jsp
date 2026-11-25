<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des contrats</title>

    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: Arial, sans-serif;
            background: #f3f6fb;
            color: #263247;
            padding: 35px;
        }

        .page-container {
            max-width: 1250px;
            margin: auto;
        }

        .top-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 15px;
            margin-bottom: 30px;
        }

        .back-link {
            color: #52647c;
            text-decoration: none;
            font-weight: 600;
        }

        .back-link:hover {
            color: #167d91;
        }

        h1 {
            font-size: 28px;
            color: #172b4d;
            margin-bottom: 8px;
        }

        .subtitle {
            color: #78869a;
            margin-bottom: 25px;
        }

        .btn-add {
            display: inline-block;
            background: #087f8c;
            color: white;
            padding: 12px 19px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            transition: 0.2s;
        }

        .btn-add:hover {
            background: #066875;
        }

        .table-card {
            background: white;
            border-radius: 13px;
            padding: 22px;
            box-shadow: 0 5px 20px rgba(30, 50, 80, 0.06);
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 950px;
        }

        th {
            background: #f5f8fc;
            color: #52647c;
            font-size: 13px;
            text-align: left;
            padding: 15px 12px;
            white-space: nowrap;
        }

        td {
            padding: 15px 12px;
            border-bottom: 1px solid #edf0f5;
            font-size: 14px;
        }

        tr:last-child td {
            border-bottom: none;
        }

        tbody tr:hover {
            background: #fafcff;
        }

        .numero {
            font-weight: 700;
            color: #172b4d;
        }

        .badge {
            display: inline-block;
            padding: 6px 10px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 700;
            background: #e8eef7;
            color: #52647c;
        }

        .badge-actif {
            background: #dcfce7;
            color: #15803d;
        }

        .badge-en-attente {
            background: #fef3c7;
            color: #a16207;
        }

        .badge-suspendu {
            background: #ffedd5;
            color: #c2410c;
        }

        .badge-expire,
        .badge-resilie,
        .badge-résilié {
            background: #fee2e2;
            color: #b91c1c;
        }

        .actions {
            display: flex;
            gap: 8px;
            align-items: center;
        }

        .actions form {
            margin: 0;
        }

        .btn {
            border: none;
            border-radius: 6px;
            padding: 8px 11px;
            cursor: pointer;
            font-size: 12px;
            font-weight: 600;
        }

        .btn-edit {
            background: #e0f2fe;
            color: #0369a1;
        }

        .btn-edit:hover {
            background: #bae6fd;
        }

        .btn-cancel {
            background: #fee2e2;
            color: #b91c1c;
        }

        .btn-cancel:hover {
            background: #fecaca;
        }

        .empty-state {
            text-align: center;
            padding: 40px 15px;
            color: #78869a;
        }

        @media (max-width: 700px) {
            body {
                padding: 18px;
            }

            h1 {
                font-size: 23px;
            }

            .table-card {
                padding: 12px;
            }
        }
    </style>
</head>

<body>

<div class="page-container">

    <div class="top-bar">
        <a class="back-link"
           href="${pageContext.request.contextPath}/admin/dashboard">
            Retour au tableau de bord
        </a>

        <a class="btn-add"
           href="${pageContext.request.contextPath}/admin/ajouter-contrat">
            + Créer un contrat
        </a>
    </div>

    <h1>Gestion des contrats</h1>
    <p class="subtitle">Consultez et gérez les contrats d'assurance.</p>

    <div class="table-card">

        <c:choose>
            <c:when test="${empty contrats}">
                <div class="empty-state">
                    Aucun contrat n'est enregistré pour le moment.
                </div>
            </c:when>

            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Numéro</th>
                            <th>Client</th>
                            <th>Type d'assurance</th>
                            <th>Date de début</th>
                            <th>Date de fin</th>
                            <th>Montant</th>
                            <th>Statut</th>
                            <th>Actions</th>
                        </tr>
                    </thead>

                    <tbody>
                        <c:forEach var="contrat" items="${contrats}">
                            <tr>
                                <td>${contrat.id}</td>

                                <td class="numero">
                                    <c:out value="${contrat.numeroContrat}"/>
                                </td>

                                <td>
                                    <c:out value="${contrat.client.nom}"/>
                                    <c:out value="${contrat.client.prenom}"/>
                                </td>

                                <td>
                                    <c:out value="${contrat.typeAssurance.nom}"/>
                                </td>

                                <td>${contrat.dateDebut}</td>
                                <td>${contrat.dateFin}</td>
                                <td>${contrat.montant}</td>

                                <td>
                                    <c:choose>
                                        <c:when test="${contrat.statut == 'ACTIF'}">
                                            <span class="badge badge-actif">Actif</span>
                                        </c:when>
                                        <c:when test="${contrat.statut == 'EN_ATTENTE'}">
                                            <span class="badge badge-en-attente">En attente</span>
                                        </c:when>
                                        <c:when test="${contrat.statut == 'SUSPENDU'}">
                                            <span class="badge badge-suspendu">Suspendu</span>
                                        </c:when>
                                        <c:when test="${contrat.statut == 'EXPIRE'}">
                                            <span class="badge badge-expire">Expiré</span>
                                        </c:when>
                                        <c:when test="${contrat.statut == 'RESILIE' || contrat.statut == 'RÉSILIÉ'}">
                                            <span class="badge badge-resilie">Résilié</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge">
                                                <c:out value="${contrat.statut}"/>
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <div class="actions">

                                        <form action="${pageContext.request.contextPath}/admin/modifier-contrat"
                                              method="get">
                                            <input type="hidden" name="id" value="${contrat.id}">
                                            <button type="submit" class="btn btn-edit">
                                                Modifier
                                            </button>
                                        </form>

                                        <form action="${pageContext.request.contextPath}/admin/annuler-contrat"
                                              method="post"
                                              onsubmit="return confirm('Voulez-vous vraiment annuler ce contrat ?');">
                                            <input type="hidden" name="id" value="${contrat.id}">
                                            <button type="submit" class="btn btn-cancel">
                                                Annuler
                                            </button>
                                        </form>

                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>

    </div>

</div>

</body>
</html>