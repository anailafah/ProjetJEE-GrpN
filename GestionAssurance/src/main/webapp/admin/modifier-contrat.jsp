<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modifier un contrat</title>
</head>
<body>

    <h1>Modifier le contrat</h1>

    <form action="modifier-contrat" method="post">

        <input type="hidden" name="id" value="${contrat.id}">

        <label>Numéro du contrat :</label>
        <input type="text" name="numeroContrat"
               value="${contrat.numeroContrat}" required>
        <br><br>

        <label>Client :</label>
        <select name="clientId" required>
            <c:forEach var="client" items="${clients}">
                <option value="${client.id}"
                    ${client.id == contrat.client.id ? 'selected' : ''}>
                    ${client.nom} ${client.prenom}
                </option>
            </c:forEach>
        </select>
        <br><br>

        <label>Type d'assurance :</label>
        <select name="typeAssuranceId" required>
            <c:forEach var="type" items="${types}">
                <c:if test="${type.actif}">
                    <option value="${type.id}"
                        ${type.id == contrat.typeAssurance.id ? 'selected' : ''}>
                        ${type.nom}
                    </option>
                </c:if>
            </c:forEach>
        </select>
        <br><br>

        <label>Date de début :</label>
        <input type="date" name="dateDebut"
               value="${contrat.dateDebut}" required>
        <br><br>

        <label>Date de fin :</label>
        <input type="date" name="dateFin"
               value="${contrat.dateFin}" required>
        <br><br>

        <label>Montant :</label>
        <input type="number" name="montant"
               value="${contrat.montant}"
               step="0.01" min="0.01" required>
        <br><br>

        <label>Garanties :</label>
        <select name="garantieIds" multiple required>
            <c:forEach var="garantie" items="${garanties}">
                <c:if test="${garantie.actif}">
                    <option value="${garantie.id}"
                        ${contrat.garanties.contains(garantie) ? 'selected' : ''}>
                        ${garantie.nom}
                    </option>
                </c:if>
            </c:forEach>
        </select>

        <p>Maintiens Ctrl pour sélectionner plusieurs garanties.</p>

        <button type="submit">Enregistrer les modifications</button>

    </form>

</body>
</html>