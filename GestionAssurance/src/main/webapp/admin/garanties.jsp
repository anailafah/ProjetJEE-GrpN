<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des garanties</title>
    <link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
	<div class="retour-dashboard">
    <a href="${pageContext.request.contextPath}/admin/dashboard">
        Retour au tableau de bord
    </a>
	</div>

    <h1>Liste des garanties</h1>
    
    <p>
    	<a href="ajouter-garantie.jsp">Ajouter une garantie</a>
	</p>

    <table border="1">
        <tr>
            <th>ID</th>
            <th>Nom</th>
            <th>Description</th>
            <th>Montant maximal</th>
            <th>Statut</th>
            <th>Action</th>
        </tr>

        <c:forEach var="garantie" items="${garanties}">
            <tr>
                <td>${garantie.id}</td>
                <td>${garantie.nom}</td>
                <td>${garantie.description}</td>
                <td>${garantie.montantCouverture}</td>
                <td>
                    <c:choose>
                        <c:when test="${garantie.actif}">
                            Actif
                        </c:when>
                        <c:otherwise>
                            Inactif
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
    				<form action="modifier-garantie" method="get">
        				<input type="hidden" name="id" value="${garantie.id}">
        				<button type="submit">Modifier</button>
    				</form>

    				<form action="desactiver-garantie" method="post">
        				<input type="hidden" name="id" value="${garantie.id}">
        				<button type="submit">Désactiver</button>
    				</form>
				</td>
            </tr>
        </c:forEach>

    </table>

</body>
</html>