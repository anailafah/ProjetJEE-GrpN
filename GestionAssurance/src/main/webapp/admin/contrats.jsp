<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des contrats</title>
</head>
<body>

    <h1>Liste des contrats</h1>
    <p>
    	<a href="ajouter-contrat">Créer un nouveau contrat</a>
	</p>

    <table border="1">
        <tr>
            <th>ID</th>
            <th>Numéro du contrat</th>
            <th>Client</th>
            <th>Type d'assurance</th>
            <th>Date de début</th>
            <th>Date de fin</th>
            <th>Montant</th>
            <th>Statut</th>
            <th>Action</th>
        </tr>

        <c:forEach var="contrat" items="${contrats}">
            <tr>
                <td>${contrat.id}</td>
                <td>${contrat.numeroContrat}</td>
                <td>${contrat.client.nom} ${contrat.client.prenom}</td>
                <td>${contrat.typeAssurance.nom}</td>
                <td>${contrat.dateDebut}</td>
                <td>${contrat.dateFin}</td>
                <td>${contrat.montant}</td>
                <td>${contrat.statut}</td>
                <td>
    				<form action="modifier-contrat" method="get">
       					<input type="hidden" name="id" value="${contrat.id}">
        				<button type="submit">Modifier</button>
    				</form>
    				
    				<form action="annuler-contrat" method="post" onsubmit="return confirm('Voulez-vous vraiment annuler ce contrat ?');">
						<input type="hidden" name="id" value="${contrat.id}">
    					<button type="submit">Annuler</button>
					</form>
				</td>
            </tr>
        </c:forEach>

    </table>

</body>
</html>