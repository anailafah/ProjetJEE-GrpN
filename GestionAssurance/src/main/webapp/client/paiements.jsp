<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes paiements - GestionAssurance</title>

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
    
        /* ---- Ajouts Paiements ---- */
        .section { font-size: 19px; margin: 30px 0 14px; }
        .table-container table { min-width: 650px; }
        .badge-payee, .badge-effectue { background: #dcfce7; color: #15803d; }
        .badge-en_retard { background: #fee2e2; color: #b91c1c; }
        .badge-annulee { background: #f1f5f9; color: #64748b; }
    </style>
</head>

<body>

<header>
    <h2>GestionAssurance</h2>
    <a href="${pageContext.request.contextPath}/logout">Déconnexion</a>
</header>

<main>

    <a class="retour" href="${pageContext.request.contextPath}/client/dashboard">
        ← Retour au tableau de bord
    </a>

    <div class="titre">
        <h1>Mes paiements</h1>
        <p>Suivez vos échéances et l'historique de vos règlements.</p>
    </div>

    <c:if test="${not empty erreur}">
        <div class="erreur"><c:out value="${erreur}"/></div>
    </c:if>

    <h2 class="section">Mes échéances</h2>
    <div class="table-container">
        <c:choose>
            <c:when test="${not empty echeances}">
                <table>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Contrat</th>
                            <th>Montant</th>
                            <th>Reste à payer</th>
                            <th>Statut</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="e" items="${echeances}">
                            <tr>
                                <td>${e.dateEcheance}</td>
                                <td><c:out value="${e.contrat.numeroContrat}"/></td>
                                <td>${e.montant} €</td>
                                <td>${restes[e.id]} €</td>
                                <td>
                                    <span class="badge badge-${e.statut.toLowerCase()}">
                                        <c:choose>
                                            <c:when test="${e.statut == 'PAYEE'}">Payée</c:when>
                                            <c:when test="${e.statut == 'EN_RETARD'}">En retard</c:when>
                                            <c:when test="${e.statut == 'EN_ATTENTE'}">À venir</c:when>
                                            <c:when test="${e.statut == 'ANNULEE'}">Annulée</c:when>
                                            <c:otherwise><c:out value="${e.statut}"/></c:otherwise>
                                        </c:choose>
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="vide">
                    <h3>Aucune échéance</h3>
                    <p>Vos échéances apparaîtront ici une fois votre contrat activé.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <h2 class="section">Historique de mes paiements</h2>
    <div class="table-container">
        <c:choose>
            <c:when test="${not empty paiements}">
                <table>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Contrat</th>
                            <th>Échéance du</th>
                            <th>Montant</th>
                            <th>Mode</th>
                            <th>Référence</th>
                            <th>Statut</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${paiements}">
                            <tr>
                                <td>${p.datePaiement.format(formatDate)}</td>
                                <td><c:out value="${p.echeance.contrat.numeroContrat}"/></td>
                                <td>${p.echeance.dateEcheance}</td>
                                <td>${p.montant} €</td>
                                <td><c:out value="${p.modePaiement}"/></td>
                                <td><c:out value="${p.referencePaiement}"/></td>
                                <td>
                                    <span class="badge badge-${p.statut.toLowerCase()}">
                                        <c:choose>
                                            <c:when test="${p.statut == 'EFFECTUE'}">Effectué</c:when>
                                            <c:when test="${p.statut == 'EN_ATTENTE'}">En attente de validation</c:when>
                                            <c:otherwise><c:out value="${p.statut}"/></c:otherwise>
                                        </c:choose>
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="vide">
                    <h3>Aucun paiement</h3>
                    <p>Vous n'avez encore effectué aucun paiement.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

</main>

</body>
</html>
