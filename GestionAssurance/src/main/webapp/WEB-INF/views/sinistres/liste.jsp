<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sinistres - GestionAssurance</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sinistres.css">
</head>

<body class="espace-employe">

<%@ include file="/WEB-INF/views/sinistres/menu-employe.jspf" %>

<main class="main">

    <header class="topbar">
        <div>
            <h1>Gestion des sinistres</h1>
            <p class="sous-titre" style="margin-bottom:0;">
                <c:choose>
                    <c:when test="${espace == 'admin'}">Consultation de l'ensemble des sinistres déclarés.</c:when>
                    <c:otherwise>Analysez les déclarations et faites avancer les dossiers.</c:otherwise>
                </c:choose>
            </p>
        </div>

        <div class="profile">
            <c:out value="${sessionScope.prenom}"/>
            <c:out value="${sessionScope.nom}"/>
        </div>
    </header>

    <a class="retour" href="${ctx}/${espace}/dashboard">← Retour au tableau de bord</a>

    <c:if test="${not empty erreur}">
        <div class="erreur"><c:out value="${erreur}"/></div>
    </c:if>

    <%-- Compteurs par statut : un clic filtre la liste --%>
    <div class="pastilles">
        <a class="pastille ${empty statutChoisi ? 'active' : ''}" href="${ctx}/${espace}/sinistres">
            <strong>${total}</strong> Tous
        </a>
        <c:forEach var="s" items="${statuts}">
            <a class="pastille ${statutChoisi == s ? 'active' : ''}"
               href="${ctx}/${espace}/sinistres?statut=${s}">
                <strong>${compteurs[s]}</strong>
                <span class="badge statut-${s}"><c:out value="${s.libelle}"/></span>
            </a>
        </c:forEach>
    </div>

    <div class="carte">

        <form class="filtres" action="${ctx}/${espace}/sinistres" method="get">
            <input type="text" name="recherche" value="<c:out value='${recherche}'/>"
                   placeholder="Numéro de sinistre ou de contrat, nom ou email du client...">

            <select name="statut">
                <option value="">Tous les statuts</option>
                <c:forEach var="s" items="${statuts}">
                    <option value="${s}" ${statutChoisi == s ? 'selected' : ''}>
                        <c:out value="${s.libelle}"/>
                    </option>
                </c:forEach>
            </select>

            <button type="submit" class="btn">Rechercher</button>
            <a class="btn btn-secondaire" href="${ctx}/${espace}/sinistres">Réinitialiser</a>
        </form>

        <c:choose>
            <c:when test="${not empty sinistres}">
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Numéro</th>
                                <th>Déclaré le</th>
                                <th>Client</th>
                                <th>Contrat</th>
                                <th>Type</th>
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
                                    <td>
                                        <c:out value="${sinistre.contrat.client.prenom}"/>
                                        <c:out value="${sinistre.contrat.client.nom}"/>
                                    </td>
                                    <td><c:out value="${sinistre.contrat.numeroContrat}"/></td>
                                    <td><c:out value="${sinistre.typeSinistre.libelle}"/></td>
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
                                           href="${ctx}/${espace}/sinistre?id=${sinistre.id}">
                                            <c:choose>
                                                <c:when test="${espace == 'agent'}">Traiter</c:when>
                                                <c:otherwise>Consulter</c:otherwise>
                                            </c:choose>
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
                    <h3>Aucun sinistre</h3>
                    <p>Aucune déclaration ne correspond à ces critères.</p>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

</main>

</body>
</html>
