<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <title>Gestion des types d'assurance</title>
    <link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/admin.css">
</head>

<body>
	<div class="retour-dashboard">
    <a href="${pageContext.request.contextPath}/admin/dashboard">
        Retour au tableau de bord
    </a>
	</div>

    <h1>Liste des types d'assurance</h1>

    <p>
    	<a href="ajouter-type-assurance.jsp">Ajouter un type d'assurance</a>
    </p>

    <%
        String messageSucces = (String) session.getAttribute("messageSucces");
        if (messageSucces != null) {
    %>
        <p style="color: green;">
            <%= messageSucces %>
        </p>
    <%
            session.removeAttribute("messageSucces");
        }
    %>

    <%
        String messageErreur = (String) request.getAttribute("messageErreur");
        if (messageErreur != null) {
    %>
        <p style="color: red;">
            <%= messageErreur %>
        </p>
    <%
        }
    %>

    <table border="1" cellpadding="10">

        <thead>
            <tr>
                <th>ID</th>
                <th>Nom</th>
                <th>Description</th>
                <th>Durée (mois)</th>
                <th>Tarif de base</th>
                <th>État</th>
                <th>Action</th>
            </tr>
        </thead>

        <tbody>

            <c:forEach var="type" items="${typesAssurance}">

                <tr>
                    <td>${type.id}</td>
                    <td>${type.nom}</td>
                    <td>${type.description}</td>
                    <td>${type.dureeMois}</td>
                    <td>${type.tarifBase}</td>

                    <td>
                        <c:choose>
                            <c:when test="${type.actif}">
                                Actif
                            </c:when>
                            <c:otherwise>
                                Inactif
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <td>
    					<form action="modifier-type-assurance" method="get">
        					<input type="hidden" name="id" value="${type.id}">
        					<button type="submit">Modifier</button>
    					</form>

    					<form action="desactiver-type-assurance" method="post">
        					<input type="hidden" name="id" value="${type.id}">
        					<button type="submit">Désactiver</button>
    					</form>
					</td>

                </tr>

            </c:forEach>

        </tbody>

    </table>

</body>
</html>