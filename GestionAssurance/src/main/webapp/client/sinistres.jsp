<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes sinistres - GestionAssurance</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sinistres.css">
</head>

<body class="espace-client">

<header>
    <h2>GestionAssurance</h2>
    <a href="${pageContext.request.contextPath}/logout">Déconnexion</a>
</header>

<main>

    <a class="retour" href="${pageContext.request.contextPath}/client/dashboard">
        ← Retour au tableau de bord
    </a>

    <h1>Mes sinistres</h1>
    <p class="sous-titre">Suivez l'état de vos déclarations et retrouvez leur historique.</p>

    <c:if test="${not empty succes}">
        <div class="succes"><c:out value="${succes}"/></div>
    </c:if>

    <c:if test="${not empty erreur}">
        <div class="erreur"><c:out value="${erreur}"/></div>
    </c:if>

    <div class="carte">

        <div class="carte-titre">
            <h2>Historique de mes déclarations</h2>
            <a class="btn" href="${pageContext.request.contextPath}/client/declarer-sinistre">
                + Déclarer un sinistre
            </a>
        </div>

        <c:choose>
            <c:when test="${not empty sinistres}">
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Numéro</th>
                                <th>Déclaré le</th>
                                <th>Date du sinistre</th>
                                <th>Type</th>
                                <th>Contrat</th>
                                <th>Montant estimé</th>
                                <th>Indemnisation</th>
                                <th>Statut</th>
                                <th></th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="sinistre" items="${sinistres}">
                                <tr>
                                    <td><strong><c:out value="${sinistre.numeroSinistre}"/></strong></td>
                                    <td>${fn:replace(fn:substring(sinistre.dateDeclaration, 0, 16), 'T', ' à ')}</td>
                                    <td>${sinistre.dateSinistre}</td>
                                    <td><c:out value="${sinistre.typeSinistre.libelle}"/></td>
                                    <td><c:out value="${sinistre.contrat.numeroContrat}"/></td>
                                    <td>${sinistre.montantDemande} €</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty sinistre.montantIndemnisation}">
                                                ${sinistre.montantIndemnisation} €
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <span class="badge statut-${sinistre.statut}">
                                            <c:out value="${sinistre.statut.libelle}"/>
                                        </span>
                                    </td>
                                    <td>
                                        <a class="btn btn-petit"
                                           href="${pageContext.request.contextPath}/client/sinistre?id=${sinistre.id}">
                                            Voir
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>

            <c:otherwise>
                <div class="vide">
                    <h3>Aucun sinistre déclaré</h3>
                    <p>Vos déclarations apparaîtront ici.</p>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

</main>

</body>
</html>
